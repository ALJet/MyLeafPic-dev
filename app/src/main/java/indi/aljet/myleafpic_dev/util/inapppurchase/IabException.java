package indi.aljet.myleafpic_dev.util.inapppurchase;

/**
 * Created by PC-LJL on 2017/11/9.
 */

public class IabException extends Exception {
    IabResult mResult;

    public IabException(IabResult r){
        this(r,null);
    }

    public IabException(int response,String message){
        this(new IabResult(response,message));
    }

    public IabException(IabResult r,Exception cause){
        super(r.getMessage(),cause);
        mResult = r;
    }

    public IabException(int response,String message,
                        Exception cause){
        this(new IabResult(response,message),
                cause);
    }

    public IabResult getResult(){
        return mResult;
    }
}
