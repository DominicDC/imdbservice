package com.dc.rest.imdbservice.service;

import com.dc.rest.imdbservice.exception.ImdbServiceException;
/***
 ** Author: Dominic Coutinho
 ** Description: Download service skeleton
 */
public interface DownloadService {

    void downloadData(String imdbUrl) throws ImdbServiceException;
}
