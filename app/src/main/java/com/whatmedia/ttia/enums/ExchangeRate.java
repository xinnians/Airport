package com.whatmedia.ttia.enums;

import com.whatmedia.ttia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo_mac on 2017/8/6.
 */

public enum ExchangeRate {
    TAG_USD(R.string.currency_conversion_usd, R.drawable.flag_usd),
    TAG_HKD(R.string.currency_conversion_hkd, R.drawable.flag_hkd),
    TAG_GBP(R.string.currency_conversion_gbp, R.drawable.flag_gbp),
    TAG_AUD(R.string.currency_conversion_aud, R.drawable.flag_aud),
    TAG_CAD(R.string.currency_conversion_cad, R.drawable.flag_cad),
    TAG_SGD(R.string.currency_conversion_sgd, R.drawable.flag_sgd),
    TAG_CHF(R.string.currency_conversion_chf, R.drawable.flag_chf),
    TAG_JPY(R.string.currency_conversion_jpy, R.drawable.flag_jpy),
    TAG_ZAR(R.string.currency_conversion_zar, R.drawable.flag_zar),
    TAG_SEK(R.string.currency_conversion_sek, R.drawable.flag_sek),
    TAG_NZD(R.string.currency_conversion_nzd, R.drawable.flag_nzd),
    TAG_THB(R.string.currency_conversion_thb, R.drawable.flag_thb),
    TAG_PHP(R.string.currency_conversion_php, R.drawable.flag_php),
    TAG_IDR(R.string.currency_conversion_idr, R.drawable.flag_idr),
    TAG_EUR(R.string.currency_conversion_eur, R.drawable.flag_eur),
    TAG_KRW(R.string.currency_conversion_krw, R.drawable.flag_krw),
    TAG_VND(R.string.currency_conversion_vnd, R.drawable.flag_vnd),
    TAG_MYR(R.string.currency_conversion_myr, R.drawable.flag_myr),
    TAG_CNY(R.string.currency_conversion_cny, R.drawable.flag_cny),
    TAG_TWD(R.string.currency_conversion_twd, R.drawable.flag_twd);

    private int title;
    private int icon;

    ExchangeRate(int title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Get item by tag
     *
     * @param tag
     * @return
     */
    public static ExchangeRate getItemByTag(ExchangeRate tag) {
        return ExchangeRate.valueOf(tag.name());
    }

    /**
     * Get item by position
     *
     * @param position
     * @return
     */
    public static ExchangeRate getItemByPosition(int position) {
        return ExchangeRate.values()[position];
    }

    /**
     * get page data
     *
     * @return
     */
    public static List<ExchangeRate> getPage() {
        List<ExchangeRate> list = new ArrayList<>();

        for (ExchangeRate item : ExchangeRate.values()) {
            list.add(item);
        }
        return list;
    }
}