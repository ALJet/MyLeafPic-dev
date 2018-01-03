package indi.aljet.myleafpic_dev.util.inapppurchase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PC-LJL on 2017/11/9.
 */

public class SkuDetails {
    private final String mItemType;
    private final String mSku;
    private final String mType;
    private final String mPrice;
    private final long mPriceAmountMicros;
    private final String mPriceCurrencyCode;
    private final String mTitle;
    private final String mDescription;
    private final String mJson;


    public SkuDetails(String jsonSkuDetails)
        throws JSONException{
        this(IabHelper.ITEM_TYPE_INAPP,
                jsonSkuDetails);
    }

    public SkuDetails(String itemType,
                      String jsonSkuDetails)
        throws JSONException{
        mItemType = itemType;
        mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(mJson);
        mSku = o.optString("productId");
        mType = o.optString("type");
        mPrice = o.optString("price");
        mPriceAmountMicros = o.optLong("price_amount_micros");
        mPriceCurrencyCode = o.optString("price_currency_code");
        mTitle = o.optString("title");
        mDescription = o.optString("description");
    }

    public String getmItemType() {
        return mItemType;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getSku() {
        return mSku;
    }

    public String getmType() {
        return mType;
    }

    public String getmPrice() {
        return mPrice;
    }

    public long getmPriceAmountMicros() {
        return mPriceAmountMicros;
    }

    public String getmPriceCurrencyCode() {
        return mPriceCurrencyCode;
    }

    public String getmTitle() {
        return mTitle;
    }
}
