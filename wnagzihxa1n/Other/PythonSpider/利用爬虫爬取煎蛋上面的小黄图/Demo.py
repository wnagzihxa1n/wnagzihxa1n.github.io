#coding:utf-8

import requests
import threading
import Queue
import re
import urllib
import time
import shutil

header = {	
		'Host': 'jandan.net',
		'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0',
		'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
		'Accept-Language': 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
		'Accept-Encoding': 'gzip, deflate',
		'Connection': 'close',
		'Upgrade-Insecure-Requests': '1',
	}

Bad_urls = []

class JiandanSpider(threading.Thread):
	"""docstring for JiandanSpider"""
	def __init__(self, queue):
		super(JiandanSpider, self).__init__()
		self.queue = queue

	def run(self):
		while not self.queue.empty():
			url = self.queue.get_nowait()
			self.spider(url)
		
	def spider(self, url):
		# print url
		html = requests.get(url = url, headers = header)
		imgs = re.findall('<img src="(.*?)" /></p>', html.content)
		gifs = re.findall('org_src="(.*?)" ', html.content)
		for img in imgs:
			img_name = img.split('/')[-1]
			self.dump_img(img, img_name)
		for gif in gifs:
			gif_name = gif.split('/')[-1]
			self.dump_img(gif, gif_name)

	def dump_img(self, url, img_name):
		try:
			if len(url) == 0:
				return
			if self.checkURLReachable(url) == True:
				return
			if url[0] != 'h':
				url = 'http:' + url
			try:
				img_stream = requests.get(url = url.strip(), stream = True, timeout = 5, allow_redirects = False)
				if img_stream.status_code == 200:
					with open('img/' + img_name, 'wb') as f:
						img_stream.raw.decode_content = True
						shutil.copyfileobj(img_stream.raw, f)
			except Exception as e:
				print '\'' + url + '\','
		except urllib.ContentTooShortError:
			dump_img(url, img_name)

	def checkURLReachable(self, url):
		Black_List = ['farm3.static.flickr.com', 'farm4.static.flickr.com', 'farm5.static.flickr.com', 'farm6.static.flickr.com', 'farm7.static.flickr.com', 'k.min.us', 'jmdou.com', 'i.min.us', 
						'www.tumblr.com', '24.media.tumblr.com', '25.media.tumblr.com', '26.media.tumblr.com', '27.media.tumblr.com', '28.media.tumblr.com', '29.media.tumblr.com', '30.media.tumblr.com', 
						'img.ffffound.com', 'static.lisi.com.cn', 'farm8.staticflickr.com', 'farm7.staticflickr.com', 'a8.sphotos.ak.fbcdn.net', 'cdnimg.visualizeus.com', '103.imagebam.com',
						'data.tumblr.com', 'www2.ff369.com', 'm2.img.libdd.com', 'm1.img.libdd.com', 'm3.img.libdd.com', 'i1201.photobucket.com', 'fs1.clubzone.cn','105.imagebam.com', '102.imagebam.com', 
						'106.imagebam.com', '101.imagebam.com', ]
		url = url.split('/')[2]
		if url in Black_List:
			return True
		return False


def main():
	queue = Queue.Queue()

	for i in range(1, 2365):
		url = 'http://jandan.net/ooxx/page-' + str(i) + '#comments'
		queue.put(url)

	threads = []
	threads_count = 80

	for i in range(threads_count):
		threads.append(JiandanSpider(queue))

	for thread in threads:
		thread.start()

	for thread in threads:
		thread.join()

if __name__ == '__main__':
	main()