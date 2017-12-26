#coding : utf-8

import requests
import json
import time

def main():
	headers = {
  		"Accept-Encoding" : "gzip, deflate",
  		"User-Agent" : "gzip,  My Python requests library example client or username"
  	}
	
	f = open('SHA256.txt', 'r')
	f_result = open('result.txt', 'w')
	searchCount = 0
	try:
		while True:
			line = f.readline().strip()
			params = {
				'apikey' : '1d3d1aa5f94755d8051973c95ab46d2b5f521c7966a93014b69adc559efc6c60', 
				'resource' : line
			}
			if line and (len(line) == 64 or len(line) == 40 or len(line) == 32):
				searchCount = searchCount + 1
				response = requests.get('https://www.virustotal.com/vtapi/v2/file/report', params=params, headers=headers)
				if response.text != '':
					data = json.loads(response.text)
					# print data['sha1'], data['sha256'], data['md5']
					f_result.write(data['md5'] + '\n')
			elif line:
				print 'FBI warning : {' + line + '} not belongs to MD5/SHA1/SHA256'
			else:	
				break
			if searchCount % 4 == 0:
				time.sleep(60)
	except IOError, error:
		print "Caught error : " + error.message
	finally:
		f.close()
		f_result.close()

if __name__ == '__main__':
	main()