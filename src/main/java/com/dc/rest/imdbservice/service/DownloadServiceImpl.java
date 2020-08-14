package com.dc.rest.imdbservice.service;

import com.dc.rest.imdbservice.exception.ErrorMessage;
import com.dc.rest.imdbservice.exception.ImdbServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/***
 ** Author: Dominic Coutinho
 ** Description: This class contains code to download zip files from IMDB website and store it at ../feeds
 */
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TaskExecutor executor;

    private Pattern patternTag, patternLink;

    private Matcher matcherTag, matcherLink;

    private CountDownLatch latch;
    private Map<String, String> fileMap = new HashMap<>();

    private static final String HTML_A_TAG_PATTERN = "(?i)<ul><a([^>]+)>(.+?)</a></ul>";
    private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    private static final String fp = Paths.get(".", "feeds").normalize().toAbsolutePath().toString() + File.separator;

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServiceImpl.class);

    public DownloadServiceImpl() {
	latch = new CountDownLatch(7);
	patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
	patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);

    }

    public void downloadData(String url) throws ImdbServiceException {
	try {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
	    HttpEntity requestEntity = new HttpEntity<>(null, headers);
	    ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

	    HttpStatus responseStatus = result.getStatusCode();
	    if (!responseStatus.is2xxSuccessful()) {
		throw new ImdbServiceException(ErrorMessage.IMDB_ERROR);
	    }
	    Map<String, String> filesToDowload = getFileToBeDownloaded(result.getBody());
	    download(filesToDowload);
	    latch.await();

	} catch (Exception e) {
	    throw new ImdbServiceException(ErrorMessage.DOWNLOAD_ERROR, e);
	}
    }

    public Map<String, String> getFileToBeDownloaded(String data) {
	if (data != null) {
	    matcherTag = patternTag.matcher(data);
	    while (matcherTag.find()) {
		String href = matcherTag.group(1); // href
		String linkText = matcherTag.group(2); // link text
		matcherLink = patternLink.matcher(href);
		while (matcherLink.find()) {
		    String link = matcherLink.group(1); // link
		    fileMap.put(linkText, link);
		}
	    }
	}
	return fileMap;
    }

    public void download(Map<String, String> files) throws Exception  {

	for (Entry<String, String> entry : files.entrySet()) {
	    String url = entry.getValue();
	    String gzipFile = entry.getKey();
	    String unzipFile = fp + gzipFile.substring(0, gzipFile.lastIndexOf('.'));

	    // submit the request for file download
	    executor.execute(() -> {
			try {
				System.out.println("Submiting download of  " + gzipFile + " and saving as " + unzipFile);
				DownloadServiceImpl.this.downloadUnzipFile(url, unzipFile);
				//entry.setValue(unzipFile);
				latch.countDown();
			} catch (IOException ex) {
				LOGGER.error(ErrorMessage.DOWNLOAD_ERROR + ":" + gzipFile);
			}
		});
	}
    }

    private void downloadUnzipFile(String link, String outputFile) throws IOException {
	URL url = new URL(link);
	int count = 0;

	try (ReadableByteChannel in = Channels
		.newChannel(new GZIPInputStream(new BufferedInputStream(url.openStream())));
		WritableByteChannel out = Channels.newChannel(new FileOutputStream(outputFile))) {
	    ByteBuffer buffer = ByteBuffer.allocate(6553600);
	    while (in.read(buffer) != -1) {
		buffer.flip();
		out.write(buffer);
		buffer.clear();
		count++;
	    }
	    LOGGER.info("Download completed of unzipped file " + outputFile + " in " + count + " iterations");
	}
    }
}
