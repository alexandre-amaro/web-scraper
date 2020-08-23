package com.aamaro.service;

import org.springframework.stereotype.Service;

import com.aamaro.common.dto.LinkDto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Collects all the links from an URL recursively using the
 * JSoup library
 */

@Service
public class Scraper {
	
	public void getLinks(String url, int startLevel, int maxLevel, LinkDto linkDto) throws IOException {
		
		linkDto.setUrl(url);
		linkDto.setLinks(new ArrayList<LinkDto>());			
		
		System.out.println(url + ", " + startLevel);
		startLevel++;
		
		if (startLevel <= maxLevel) {
			
			try {
				
				if (url.startsWith("www.")) {
					url += "http://" + url;
				}
				Document doc = Jsoup.connect(url).get();
				Elements links = doc.select("a[href]");
							
				for (Element link : links) {
					LinkDto linkDtoChild = new LinkDto();
					getLinks(link.attr("abs:href"), startLevel, maxLevel, linkDtoChild);
					linkDto.getLinks().add(linkDtoChild);
				}
			}
			catch (Exception e) {
				linkDto.setError(e.getMessage());
			}
		}				
	}
}
