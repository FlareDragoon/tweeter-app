package edu.byu.cs.tweeter.model.net.response;

public class GetCountResponse extends Response {
    Integer count;


    public GetCountResponse(String message) {
        super(false, message);
    }

    public GetCountResponse(Integer count) {
        super(true);
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
