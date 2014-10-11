# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import requests
import os


class ScrapeBatterseaPipeline(object):
    def process_item(self, item, spider):
        filename = os.path.join('images', item['images'][0]['path'])
        image = open(filename, 'rb')
        data = {'name': item['name'], 'age': '1', 'shelter': '1', 'species': 'D', 'profile': item['description']}
        data['name'] = 'morris'
        data['profile'] = 'a dog'
        files = {'image': image}
        print 'Here goes...'
        print data
        print files
        response = requests.post('http://localhost:9000/api/pet', data=data, files=files)
        print 'Got response...'
        print response.content
        print response.status_code
        return item
