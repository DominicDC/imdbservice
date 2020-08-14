CREATE TABLE IF NOT EXISTS title_basics         --Details of titles and associated episodes
(
tb_title_id          VARCHAR(20) PRIMARY KEY     --alphanumeric unique identifier of the title
,tb_title_type       VARCHAR(50)                 --the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)
,tb_primary_title    VARCHAR(500)                --the more popular title / the title used by the filmmakers on promotional materials at the point of release
,tb_original_title   VARCHAR(500)                --original title, in the original language
,tb_is_adult         INTEGER                     --0: non-adult title; 1: adult title
,tb_start_year       INTEGER                     --represents the release year of a title. In the case of TV Series, it is the series start year
,tb_end_year         INTEGER                     --TV Series end year. ‘\N’ for all other title types
,tb_runtime_minutes  VARCHAR(20)                 --primary runtime of the title, in minutes
,tb_genres           VARCHAR(100)                --includes up to three genres associated with the title
);

CREATE TABLE IF NOT EXISTS title_akas  --(Extension of title basics)
(
 ta_id                VARCHAR(50) PRIMARY KEY
,ta_title_id          VARCHAR(20)                     -- a tconst, an alphanumeric unique identifier of the title --FK tb_titleid
,ta_ordering          INTEGER                         -- a number to uniquely identify rows for a given titleId
,ta_title             VARCHAR(1500)                   -- the localized title
,ta_region            VARCHAR(10)                     -- the region for this version of the title
,ta_language          VARCHAR(100)                    -- the language of the title
,ta_types             VARCHAR(100)                    -- Enumerated set of attributes for this alternative title. One or more of the following: "alternative", "dvd", "festival", "tv", "video", "working", "original", "imdbDisplay". New values may be added in the future without warning
,ta_attributes        VARCHAR(100)                    -- Additional terms to describe this alternative title, not enumerated
,ta_is_original_title INTEGER                         -- 0: not original title; 1: original title
);

CREATE TABLE IF NOT EXISTS name_basics      --Actor/director/writer/etc details
(
 nb_cast_id              VARCHAR(20) PRIMARY KEY -- alphanumeric unique identifier of the name/person
,nb_primary_name         VARCHAR(100)            -- name by which the person is most often credited
,nb_birth_year           INTEGER                 --  in YYYY format
,nb_death_year           INTEGER                 --  in YYYY format if applicable, else '\N'
,nb_primary_profession   VARCHAR(100)            -- the top-3 professions of the person
,nb_known_for_titles     TEXT --VARCHAR(1000)    -- titles the person is known for
);

CREATE TABLE IF NOT EXISTS title_episodes        -- Title season and episode details
(
 te_title_id         VARCHAR(20) PRIMARY KEY     -- alphanumeric identifier of episode               FK - tb_titleid
,te_parent_title_id  VARCHAR(20)                 -- alphanumeric identifier of the parent TV Series  FK - tb_titleid
,te_season_number    INTEGER                     -- season number the episode belongs to
,te_episode_number   INTEGER                     -- episode number of the tconst in the TV series
);  

CREATE TABLE IF NOT EXISTS title_principals   -- Primary cast details 
(
 tp_id          VARCHAR(50) PRIMARY KEY
,tp_title_id    VARCHAR(20)     -- alphanumeric unique identifier of the title              FK - tb_titleid
,tp_ordering    INTEGER         -- a number to uniquely identify rows for a given titleId
,tp_cast_id     VARCHAR(20)     -- alphanumeric unique identifier of the name/person      FK - nb_castid
,tp_category    VARCHAR(100)    -- the category of job that person was in
,tp_job         VARCHAR(1000)    -- the specific job title if applicable, else '\N'
,tp_characters  TEXT --VARCHAR(1000)    -- the name of the character played if applicable, else '\N'
,UNIQUE (tp_title_id, tp_cast_id, tp_ordering)
);

CREATE TABLE IF NOT EXISTS title_ratings        --Title/season/episodes and ratings mapping 
(
 tr_title_id        VARCHAR(20) PRIMARY KEY     -- alphanumeric unique identifier of the title
,tr_average_rating  DOUBLE PRECISION            -- weighted average of all the individual user ratings
,tr_num_votes       INTEGER                     -- number of votes the title has received
);

CREATE TABLE IF NOT EXISTS title_crew           
(
 tc_id           VARCHAR(50) PRIMARY KEY
,tc_title_id     VARCHAR(20)         -- This can be set as a Key
,tc_director_id  TEXT --VARCHAR(1000)        -- comma seperated list of director ids
,tc_writer_id    TEXT --VARCHAR(1000)        -- comma seperated list of writer ids
);

