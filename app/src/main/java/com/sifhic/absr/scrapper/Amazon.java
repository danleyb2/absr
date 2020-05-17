package com.sifhic.absr.scrapper;

import android.util.Log;
import android.util.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Amazon {
    public static final String TAG = Amazon.class.getSimpleName();

    public static ArrayList<Pair<String,Integer>> bestSellerRanks(String asin){
        String url = "https://www.amazon.com/dp/"+asin;
        try {
            String response = Amazon.run(url);
            Log.i(TAG,response);
            Document doc = Jsoup.parse(response, "http://example.com/");
            
            return parseDoc(doc);
            // System.out.println(productDetailsTableBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static ArrayList<Pair<String,Integer>> parseDoc(Document doc){
        ArrayList<Pair<String,Integer>> ranks = new ArrayList<>();

        Element productDetailsTable = doc.getElementById("productDetails_detailBullets_sections1");
        Element productDetailsTableBody = productDetailsTable.child(0);
        // Loop Trs
        Elements trElements =  productDetailsTableBody.children();
        for (Element element : trElements) {
            if (element.child(0).text().equals("Best Sellers Rank")) {
                Element td = element.child(1);
                Element span = td.child(0);

                Elements rankSpans =  span.children();
                for (Element rankSpan : rankSpans) {
                    if (rankSpan.tagName().toUpperCase().equals("SPAN")) {
                        System.out.println(rankSpan);
                        String rankSpanText = rankSpan.ownText();
                        String rank = rankSpanText.split(" ")[0];
                        rank = rank.replaceAll(",","");
                        rank = rank.replaceAll("#","");

                        Element link = rankSpan.getElementsByTag("a").first();
                        String category = link.text();
                        System.out.println("Rank: "+rank+" Category: "+category);

                        ranks.add(new Pair<>(category, Integer.parseInt(rank)));

                    }
                }
            }
        }

        return ranks;

    }


    static String run(String url) throws IOException {
         OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36")
                .addHeader("Origin", "https://www.directechs.com/Account/Login")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    
}
