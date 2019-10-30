package com.bk.olympia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class OlympiaApplication {

	public static void main(String[] args) {
	    SpringApplication.run(OlympiaApplication.class, args);

        // Connecting to Memcached server on localhost
//        try {
//            MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
//            mcc.set("1",0, "modified");
//            System.out.println(mcc.get("1"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Connection to server sucessfully");

//        //not set data into memcached server
//        System.out.println("set status:"+mcc.set("tutorialspoint", 900, "memcached").done);
//
//        //Get value from cache
//        System.out.println("Get from Cache:"+mcc.get("tutorialspoint"));
    }

}
