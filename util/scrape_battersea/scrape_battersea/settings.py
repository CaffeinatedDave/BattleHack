# -*- coding: utf-8 -*-

# Scrapy settings for scrape_battersea project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#

BOT_NAME = 'scrape_battersea'

SPIDER_MODULES = ['scrape_battersea.spiders']
NEWSPIDER_MODULE = 'scrape_battersea.spiders'
ITEM_PIPELINES = {'scrapy.contrib.pipeline.images.ImagesPipeline': 1, 'scrape_battersea.pipelines.ScrapeBatterseaPipeline': 2}
IMAGES_STORE = 'images'
AUTOTHROTTLE_ENABLED = True

# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'scrape_battersea (+http://www.yourdomain.com)'
