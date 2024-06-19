package il.cshaifasweng.OCSFMediatorExample.client;


public abstract class BaseController {

    protected App app;

    public void setApp(App app) {
        this.app = app;
    }
    public App getApp()
    {
        return this.app;
    }

    // You can add other common methods or functionality here
}
