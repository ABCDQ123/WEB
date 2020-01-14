package com.comic.mario.ui.bean;

import java.io.Serializable;
import java.util.List;

public class WebBean implements Serializable {
    private String web;
    private SearchBean search;
    private DetailBean detail;
    private ReadBean read;
    private List<RankBean> rank;
    private List<ClassifyBean> classify;

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public SearchBean getSearch() {
        return search;
    }

    public void setSearch(SearchBean search) {
        this.search = search;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public ReadBean getRead() {
        return read;
    }

    public void setRead(ReadBean read) {
        this.read = read;
    }

    public List<RankBean> getRank() {
        return rank;
    }

    public void setRank(List<RankBean> rank) {
        this.rank = rank;
    }

    public List<ClassifyBean> getClassify() {
        return classify;
    }

    public void setClassify(List<ClassifyBean> classify) {
        this.classify = classify;
    }

    public static class SearchBean implements Serializable {

        private String title;
        private String url;
        private String mainEl;
        private String linkEl;
        private String titleEl;
        private String imageEl;
        private String introEl;
        private String prepareMethod;
        private String nextPageMethod;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMainEl() {
            return mainEl;
        }

        public void setMainEl(String mainEl) {
            this.mainEl = mainEl;
        }

        public String getLinkEl() {
            return linkEl;
        }

        public void setLinkEl(String linkEl) {
            this.linkEl = linkEl;
        }

        public String getTitleEl() {
            return titleEl;
        }

        public void setTitleEl(String titleEl) {
            this.titleEl = titleEl;
        }

        public String getImageEl() {
            return imageEl;
        }

        public void setImageEl(String imageEl) {
            this.imageEl = imageEl;
        }

        public String getIntroEl() {
            return introEl;
        }

        public void setIntroEl(String introEl) {
            this.introEl = introEl;
        }

        public String getPrepareMethod() {
            return prepareMethod;
        }

        public void setPrepareMethod(String prepareMethod) {
            this.prepareMethod = prepareMethod;
        }

        public String getNextPageMethod() {
            return nextPageMethod;
        }

        public void setNextPageMethod(String nextPageMethod) {
            this.nextPageMethod = nextPageMethod;
        }
    }

    public static class DetailBean implements Serializable {

        private String title;
        private String detailMainEl;
        private String titleEl;
        private String imageEl;
        private String authorEl;
        private String classifyEl;
        private String popularEl;
        private String statusEl;
        private String timeEl;
        private String episodeMainEl;
        private String episodeLinkEl;
        private String episodeEl;
        private String episodeSort;
        private String prepareMethod;

        public String getEpisodeSort() {
            return episodeSort;
        }

        public void setEpisodeSort(String episodeSort) {
            this.episodeSort = episodeSort;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetailMainEl() {
            return detailMainEl;
        }

        public void setDetailMainEl(String detailMainEl) {
            this.detailMainEl = detailMainEl;
        }

        public String getTitleEl() {
            return titleEl;
        }

        public void setTitleEl(String titleEl) {
            this.titleEl = titleEl;
        }

        public String getImageEl() {
            return imageEl;
        }

        public void setImageEl(String imageEl) {
            this.imageEl = imageEl;
        }

        public String getAuthorEl() {
            return authorEl;
        }

        public void setAuthorEl(String authorEl) {
            this.authorEl = authorEl;
        }

        public String getClassifyEl() {
            return classifyEl;
        }

        public void setClassifyEl(String classifyEl) {
            this.classifyEl = classifyEl;
        }

        public String getPopularEl() {
            return popularEl;
        }

        public void setPopularEl(String popularEl) {
            this.popularEl = popularEl;
        }

        public String getStatusEl() {
            return statusEl;
        }

        public void setStatusEl(String statusEl) {
            this.statusEl = statusEl;
        }

        public String getTimeEl() {
            return timeEl;
        }

        public void setTimeEl(String timeEl) {
            this.timeEl = timeEl;
        }

        public String getEpisodeMainEl() {
            return episodeMainEl;
        }

        public void setEpisodeMainEl(String episodeMainEl) {
            this.episodeMainEl = episodeMainEl;
        }

        public String getEpisodeLinkEl() {
            return episodeLinkEl;
        }

        public void setEpisodeLinkEl(String episodeLinkEl) {
            this.episodeLinkEl = episodeLinkEl;
        }

        public String getEpisodeEl() {
            return episodeEl;
        }

        public void setEpisodeEl(String episodeEl) {
            this.episodeEl = episodeEl;
        }

        public String getPrepareMethod() {
            return prepareMethod;
        }

        public void setPrepareMethod(String prepareMethod) {
            this.prepareMethod = prepareMethod;
        }
    }

    public static class ReadBean implements Serializable {

        private String jointUrl;
        private String indexEl;
        private String imageUrl;
        private String introUrl;
        private String prepareMethod;
        private String nextPageMethod;

        public String getJointUrl() {
            return jointUrl;
        }

        public void setJointUrl(String jointUrl) {
            this.jointUrl = jointUrl;
        }

        public String getIndexEl() {
            return indexEl;
        }

        public void setIndexEl(String indexEl) {
            this.indexEl = indexEl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getIntroUrl() {
            return introUrl;
        }

        public void setIntroUrl(String introUrl) {
            this.introUrl = introUrl;
        }

        public String getPrepareMethod() {
            return prepareMethod;
        }

        public void setPrepareMethod(String prepareMethod) {
            this.prepareMethod = prepareMethod;
        }

        public String getNextPageMethod() {
            return nextPageMethod;
        }

        public void setNextPageMethod(String nextPageMethod) {
            this.nextPageMethod = nextPageMethod;
        }
    }

    public static class RankBean implements Serializable {

        private String title;
        private String url;
        private String mainEl;
        private String linkEl;
        private String titleEl;
        private String imageEl;
        private String introEl;
        private String prepareMethod;
        private String nextPageMethod;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMainEl() {
            return mainEl;
        }

        public void setMainEl(String mainEl) {
            this.mainEl = mainEl;
        }

        public String getLinkEl() {
            return linkEl;
        }

        public void setLinkEl(String linkEl) {
            this.linkEl = linkEl;
        }

        public String getTitleEl() {
            return titleEl;
        }

        public void setTitleEl(String titleEl) {
            this.titleEl = titleEl;
        }

        public String getImageEl() {
            return imageEl;
        }

        public void setImageEl(String imageEl) {
            this.imageEl = imageEl;
        }

        public String getIntroEl() {
            return introEl;
        }

        public void setIntroEl(String introEl) {
            this.introEl = introEl;
        }

        public String getPrepareMethod() {
            return prepareMethod;
        }

        public void setPrepareMethod(String prepareMethod) {
            this.prepareMethod = prepareMethod;
        }

        public String getNextPageMethod() {
            return nextPageMethod;
        }

        public void setNextPageMethod(String nextPageMethod) {
            this.nextPageMethod = nextPageMethod;
        }
    }

    public static class ClassifyBean implements Serializable {

        private String title;
        private String url;
        private String mainEl;
        private String linkEl;
        private String titleEl;
        private String imageEl;
        private String introEl;
        private String prepareMethod;
        private String nextPageMethod;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMainEl() {
            return mainEl;
        }

        public void setMainEl(String mainEl) {
            this.mainEl = mainEl;
        }

        public String getLinkEl() {
            return linkEl;
        }

        public void setLinkEl(String linkEl) {
            this.linkEl = linkEl;
        }

        public String getTitleEl() {
            return titleEl;
        }

        public void setTitleEl(String titleEl) {
            this.titleEl = titleEl;
        }

        public String getImageEl() {
            return imageEl;
        }

        public void setImageEl(String imageEl) {
            this.imageEl = imageEl;
        }

        public String getIntroEl() {
            return introEl;
        }

        public void setIntroEl(String introEl) {
            this.introEl = introEl;
        }

        public String getPrepareMethod() {
            return prepareMethod;
        }

        public void setPrepareMethod(String prepareMethod) {
            this.prepareMethod = prepareMethod;
        }

        public String getNextPageMethod() {
            return nextPageMethod;
        }

        public void setNextPageMethod(String nextPageMethod) {
            this.nextPageMethod = nextPageMethod;
        }
    }
}
