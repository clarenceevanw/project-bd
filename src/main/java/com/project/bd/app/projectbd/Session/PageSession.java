package com.project.bd.app.projectbd.Session;

public class PageSession {
    private static PageSession instance = null;
    private String originPage;
    public static PageSession getInstance() {
        if (instance == null) {
            instance = new PageSession();
        }
        return instance;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }

    public String getOriginPage() {
        return originPage;
    }

}
