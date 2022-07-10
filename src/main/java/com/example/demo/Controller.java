package com.example.demo;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;



import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;



@RestController
public class Controller {
	
	private static final String template="Hello,%s";
	private final AtomicLong counter=new AtomicLong();
	
	private static final String usertemplate="Welcome ,%s";
	private final AtomicLong userCounter=new AtomicLong();
	
	
	
	// Counter 
	private final Counter requestCounter = Counter.build().name("requests_total").help("Total number of requests.")
			.labelNames("path").register();

	// Gauges 
	private final Gauge lastRequestTimestamp = Gauge.build().name("last_request_timestamp")
			.help("unix time of the last request").labelNames("path").register();

	// Histogram 
	private final Histogram requestDurationHistogram = Histogram.build().name("request_duration_histogram")
			.help("Request duration in seconds").labelNames("path")
			.buckets(0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.007, 0.008, 0.009).register();

	// Summaries 
	private final Summary requestDurationSummary = Summary.build().name("request_duration_summary")
			.help("Request duration in seconds").labelNames("path").quantile(0.75, 0.01).quantile(0.85, 0.01)
			.register();
	
	
	@GetMapping("/")
	public String fun() {
		return "Welcome";
	}
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(value="name",defaultValue="World") String name) {
		String path = "/greeting";
		
		JSONObject obj=new JSONObject();
		
		requestCounter.labels(path).inc();
//		lastRequestTimestamp.labels(path).setToCurrentTime();
//		Histogram.Timer histogramRequestTimer = requestDurationHistogram.labels(path).startTimer();
//		Summary.Timer summaryRequestTimer = requestDurationSummary.labels(path).startTimer();
		
		Greeting greet=new Greeting(counter.incrementAndGet(),String.format(template, name));
		obj.put("greet",greet.getContent());
		obj.put("id",greet.getId());
		LocalDate date=LocalDate.now();
		obj.put("Date", date);
		obj.put("Time", LocalTime.now());
//		System.out.print(obj);
		obj.put("path", path);
		return obj.toString();
	}
	
	@GetMapping("/user")
	public String newUser(@RequestParam(value="name",defaultValue="World") String name) {
		String path = "/user";
		requestCounter.labels(path).inc();
//		lastRequestTimestamp.labels(path).setToCurrentTime();
//		Histogram.Timer histogramRequestTimer = requestDurationHistogram.labels(path).startTimer();
//		Summary.Timer summaryRequestTimer = requestDurationSummary.labels(path).startTimer();
//		RestTemplate restTemplete = new RestTemplate();
		
//		ResponseEntity<User> user = restTemplete.getForEntity("https://jsonplaceholder.typicode.com/todos/10", User.class);
		
		JSONObject obj=new JSONObject();
		User user=new User(userCounter.incrementAndGet(),String.format(usertemplate, name));
		obj.put("greet",user.getName());
		obj.put("id",user.getId());
		LocalDate date=LocalDate.now();
		obj.put("Date", date);
		obj.put("Time", LocalTime.now());
		obj.put("path", path);
//		System.out.print(obj);
		
		return obj.toString();
	}
	
	
	

}
