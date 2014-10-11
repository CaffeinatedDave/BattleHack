# -*- coding: utf-8 -*-
import scrapy
import urlparse
from scrape_battersea.items import Dog
from scrapy.contrib.linkextractors import LinkExtractor
from scrapy.contrib.spiders import Rule, CrawlSpider


class DogSpider(scrapy.Spider):
    name = "dog"
    allowed_domains = ["battersea.org.uk"]
    start_urls = [
        # The gallery pages retrieve the pets via an ajax call to apexremote
        # so we would need to roll our own crawling to parse out the csrf,
        # issue our own requests, and then grab the Links for that.
        # For now, just hard code some profiles we like.
        #"http://www.battersea.org.uk/apex/webgallery?pageId=019-dogsforrehominggallery&type=dog"
        "http://www.battersea.org.uk/apex/webprofile?aid=300361&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=260302&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=226633&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307093&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=072942&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=304463&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=306848&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307261&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=057551&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307221&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=308674&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=306899&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=300371&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=278063&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307280&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=040900&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307322&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307195&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=307052&pageId=021-dogprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Dog;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=298350&pageId=043-catprofile",
        "http://www.battersea.org.uk/apex/webprofile?aid=277690&pageId=043-catprofile",
        "http://www.battersea.org.uk/apex/webprofile?aid=298317&pageId=043-catprofile",
        "http://www.battersea.org.uk/apex/webprofile?aid=300262&pageId=043-catprofile",
        "http://www.battersea.org.uk/apex/webprofile?aid=308691&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=308617&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=308618&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=300262&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=306742&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:0",
        "http://www.battersea.org.uk/apex/webprofile?aid=310160&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:1",
        "http://www.battersea.org.uk/apex/webprofile?aid=310123&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:1",
        "http://www.battersea.org.uk/apex/webprofile?aid=310264&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:1",
        "http://www.battersea.org.uk/apex/webprofile?aid=307038&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:1",
        "http://www.battersea.org.uk/apex/webprofile?aid=307639&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:2",
        "http://www.battersea.org.uk/apex/webprofile?aid=309216&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:2",
        "http://www.battersea.org.uk/apex/webprofile?aid=301840&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:3",
        "http://www.battersea.org.uk/apex/webprofile?aid=310156&pageId=043-catprofile&filterstring=centre:undefined;children:undefined;size:undefined;sex:undefined;liveWithDog:undefined;liveWithCat:undefined;breed:undefined;type:Cat;PageNumber:3",
    ]

    #rules = [Rule(LinkExtractor(allow='.*webprofile.*'), callback='parse_dog')]

    def parse(self, response):
        item = Dog()
        xpath = '//div[contains(@class, "item")]/a/img/@src'
        imgs = response.xpath(xpath).extract()
        item['image_urls'] = [urlparse.urljoin('http://www.battersea.org.uk', i) for i in imgs]
        item['name'] = response.css('#profile-page header h1.page').extract()[0]
        item['description'] = response.css('.bio span').extract()[0]
        item['species'] = 'C' if 'catprofile' in response.url else 'D'
        yield item
