package com.example.answer.bean;

// 返回 状态码 和 提示信息 和 数据结果
public class JsonResult extends JsonStatus{

    private Object result;

    public JsonResult(){
        super();
        this.result = "";
    }


    public JsonResult(String status,String msg,Object result) {
        super(status,msg);
        this.result =result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
