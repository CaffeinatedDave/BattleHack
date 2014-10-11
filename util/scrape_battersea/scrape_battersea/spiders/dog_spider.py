# -*- coding: utf-8 -*-
import scrapy
import urlparse
from scrape_battersea.items import Dog


class DogSpider(scrapy.Spider):
    name = "dog"
    allowed_domains = ["battersea.org.uk"]
    start_urls = [
        "http://www.battersea.org.uk/apex/webprofile?aid=300361&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0"
    ]

    def parse(self, response):
        item = Dog()
        xpath = '//div[@class="item "]/a/img/@src'
        imgs = response.xpath(xpath).extract()
        item['image_urls'] = [urlparse.urljoin('http://www.battersea.org.uk', i) for i in imgs]
        item['name'] = response.css('#profile-page header h1.page').extract()[0]
        item['description'] = response.css('.bio span').extract()[0]
        yield item
