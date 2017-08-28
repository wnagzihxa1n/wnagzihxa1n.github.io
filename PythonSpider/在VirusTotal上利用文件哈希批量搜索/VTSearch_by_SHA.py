# coding = utf-8

import requests
import re
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

VT_MD5_URL = 'https://www.virustotal.com/en/search/'
VT_SHA1_URL = 'https://www.virustotal.com/en/search/?query='
VT_SHA256_URL = 'https://www.virustotal.com/en/file/'

MD5 = 'c7097d3840f3bdc47edfbd578733f444'
SHA1 = 'cad0c655ebb25c9b8b5f04ba1852595fd47c1386'
SHA256 = '3e896599851231d11c06ee3f5f9677436850d3e7d745530f0a46f712e37ce082'

MD5_header = {
	'Host': 'www.virustotal.com',
	'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0',
	'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
	'Accept-Language': 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
	'Accept-Encoding': 'gzip, deflate, br',
	'Content-Type': 'application/x-www-form-urlencoded',
	'Content-Length': '38',
	'Referer': 'https://www.virustotal.com/',
	'Cookie': 'VT_PREFERRED_LANGUAGE=en',
	'Connection': 'keep-alive',
	'Upgrade-Insecure-Requests': '1'
}

SHA1_header = {
	'Host' : 'www.virustotal.com',
	'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0',
	'Accept' : 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
	'Accept-Language' : 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
	'Accept-Encoding' : 'gzip, deflate, br',
	'Cookie' : 'VT_PREFERRED_LANGUAGE=en',
	'Connection' : 'keep-alive',
	'Upgrade-Insecure-Requests' : '1'
}

SHA256_header = {
	'Host' : 'www.virustotal.com',
	'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0',
	'Accept' : 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
	'Accept-Language' : 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
	'Accept-Encoding' : 'gzip, deflate, br',
	'Cookie' : 'VT_PREFERRED_LANGUAGE=en',
	'Connection' : 'keep-alive',
	'Upgrade-Insecure-Requests' : '1'
}

def main():
	f = open('SHA256.txt', 'r')
	try:
		while True:
			line = f.readline()
			print line.strip(), len(line.strip())
			if line and len(line.strip()) == 64:
				response = requests.get(url=VT_SHA256_URL + line.strip() + '/analysis/', headers = SHA256_header)
				# print response.status_code
			elif line and len(line.strip()) == 40:
				response = requests.get(url=VT_SHA1_URL + line.strip(), headers = SHA1_header)
				# print response.status_code
			elif line and len(line.strip()) == 32:
				data = {
					'query' : line.strip()
				}
				response = requests.post(url=VT_MD5_URL, data=data, headers = MD5_header)
				# print response.status_code
			else:
				break
			md5 = re.findall(r'MD5</span> (.*?)\n</div>', str(response.text))
			sha1 = re.findall(r'SHA1</span> (.*?)\n</div>', str(response.text))
			sha256 = re.findall(r'SHA256</span> (.*?)\n</div>', str(response.text))
			print md5, sha1, sha256
	except IOError, error:
		print "Caught error : " + error.message
	finally:
		f.close()

if __name__ == '__main__':
	main()







	