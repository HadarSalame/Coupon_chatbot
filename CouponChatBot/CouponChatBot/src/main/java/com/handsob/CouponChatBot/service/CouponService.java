package com.handsob.CouponChatBot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.ValueHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    ObjectMapper om;

    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    public String searchCoupon(String keyword) throws IOException {
        return getCoupon(keyword);

    }



    private String getCoupon(String keyword) throws IOException {
        System.out.println(keyword);
        Request request = new Request.Builder()
                .url("https://www.coupy.co.il/_next/data/P4CaNGKhgeu6JffAe5WQy/store/"+keyword+".json")
                .method("GET", null)
                .addHeader("authority", "www.coupy.co.il")
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cookie", "_ga=GA1.1.2058192913.1681757798; _hjSessionUser_2999263=eyJpZCI6ImQ2MzQ2ODJhLWE2OTQtNWQ0My04NjlmLTFhY2RkMDhlYzQwNCIsImNyZWF0ZWQiOjE2ODE3NTc3OTgxMDYsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjIncludedInSessionSample_2999263=0; _hjSession_2999263=eyJpZCI6IjMzNDEwMTRhLWU2OTAtNDg0NS05YWIzLWViNmQzNDhiNWVhYSIsImNyZWF0ZWQiOjE2ODE3NTc3OTgxMTQsImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=0; _ga_HVRTYG2KF1=GS1.1.1681757797.1.1.1681757967.58.0.0")
                .addHeader("referer", "https://www.coupy.co.il/?search=%D7%9C%D7%95%D7%A0%D7%94%20%D7%A4%D7%90%D7%A8%D7%A7")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .addHeader("x-nextjs-data", "1")
                .build();
        Response response = client.newCall(request).execute();
        String res= response.body().string();
        CouponResponse cr = om.readValue(res,CouponResponse.class);

//        ArrayList<CouponDetails> couponList=new ArrayList<CouponDetails>(cr.getpageProps().getCoupons().size())

        try {
            String fullCoupom =" ";
            ArrayList<CouponDetails> couponList=new ArrayList<CouponDetails>();

            for (int i = 0; i <cr.getpageProps().getCoupons().size(); i++) {
                couponList.add(new CouponDetails(cr.getpageProps().getCoupons().get(i).getCoupon_code(),
                        cr.getpageProps().getCoupons().get(i).getDescription(),
                        cr.getpageProps().getCoupons().get(i).getCompany_site()
                ));
            }

            for (int i = 0; i < couponList.size(); i++) {
                fullCoupom += couponList.get(i).getCodeCoupon().toString();
                fullCoupom += " | " + couponList.get(i).getCouponDetails().toString();
                fullCoupom += " | " + couponList.get(i).getCompanySite().toString();
                fullCoupom += "\n";
            }
            return  fullCoupom;
        } catch (Exception e){
            e.printStackTrace();
            return "Not Found";
        }


    }

    static class CouponDetails{
        String codeCoupon;
        String couponDetails;
        String companySite;
        public CouponDetails(){}

        public CouponDetails(String codeCoupon, String couponDetails, String companySite) {
            this.codeCoupon = codeCoupon;
            this.couponDetails = couponDetails;
            this.companySite = companySite;
        }


        public String getCodeCoupon() {
            return codeCoupon;
        }

        public String getCouponDetails() {
            return couponDetails;
        }

        public String getCompanySite() {
            return companySite;
        }
    }


    static class CouponResponse {
        pageProps pageProps;

        public pageProps getpageProps() {
            return pageProps;
        }
    }
    //
    static class pageProps{
        public List<Coupons>coupons;

        public List<Coupons> getCoupons() {
            return coupons;
        }
    }

    static class Coupons{
        String coupon_code;
        String description;
        String company_site;
        public String getCoupon_code() {
            return coupon_code;
        }

        public String getDescription() {
            return description;
        }

        public String getCompany_site() {
            return company_site;
        }
    }


//    static class Store1{
//        StoreObj store;
//
//
//        public StoreObj getStore() {
//            return store;
//        }
//    }
//    static class StoreObj{
//         String name;
//
//        public String getName() {
//            return name;
//        }
//    }

}