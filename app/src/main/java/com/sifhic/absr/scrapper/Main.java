package com.sifhic.absr.scrapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

public class Main {
    OkHttpClient client = new OkHttpClient();

    public static void main(String[] args){
        String tundra17 = "https://www.amazon.com/dp/B07VBHNBHS";
        String fortinf150 ="https://www.amazon.com/dp/B07F422Y98";
        String assyf150 ="https://www.amazon.com/dp/B082TNG8SP";
        String sifhic = "https://api.sifhic.com/";

//        try {
//            String response = new Main().run(fortinf150);
//            System.out.println(response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File input = new File("/home/danleyb2/Work/absr-app/ABSR/app/src/main/java/com/sifhic/absr/scrapper/sample1.txt");
        try {
            Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
            // System.out.println(doc);

            Amazon.parseDoc(doc);
            // System.out.println(productDetailsTableBody);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (true)return;
        try {
            Document doc = Jsoup.connect("https://www.tempobet.com/league191_5_0.html")
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .header("Accept-Language", "en-US")
                    .header("Accept-Encoding", "gzip,deflate,sdch")
                    .get();

            System.out.println(doc);

            Elements tableElement = doc.select("table[class=table-a]");
            ListIterator<Element> trElementIterator = tableElement.select("tr:gt(2)").listIterator();

            while (trElementIterator.hasNext()) {

                ListIterator<Element> tdElementIterator = trElementIterator.next().select("td").listIterator();

                while (tdElementIterator.hasNext()) {

                    System.out.println(tdElementIterator.next());
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    String run(String url) throws IOException {
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
