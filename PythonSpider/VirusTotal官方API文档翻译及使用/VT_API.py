# coding : utf-8
# Author : wnagzihxa1n

import argparse
import requests
import json
import time
import sys

API_KEY = '1d3d1aa5f94755d8051973c95ab46d2b5f521c7966a93014b69adc559efc6c60'

def parse_arg():
	parser = argparse.ArgumentParser(description=None)
	parser.add_argument('--sending_and_scanning_files', help='Sending and scanning files')
	parser.add_argument('--rescanning_already_submitted_files', help='Rescanning already submitted files')
	parser.add_argument('--retrieving_file_scan_reports', help = 'Retrieving file scan reports')
	parser.add_argument('--sending_and_scanning_urls', help = 'Sending and scanning URLs')
	parser.add_argument('--retrieving_url_scan_reports', help = 'Retrieving URL scan reports')
	parser.add_argument('--retrieving_ip_address_reports', help = 'Retrieving IP address reports (includes Passive DNS)')
	parser.add_argument('--retrieving_domain_reports', help = 'Retrieving domain reports (includes Passive DNS)')
	parser.add_argument('--making_comments_on_files_and_urls', help = 'Making comments on files and URLs')
	return parser.parse_args()

def welcome():
	print '''
###############################################################################################
1.Sending and scanning files                           : --sending_and_scanning_files
2.Rescanning already submitted files                   : --rescanning_already_submitted_files
3.Retrieving file scan reports                         : --retrieving_file_scan_reports
4.Sending and scanning URLs                            : --sending_and_scanning_urls
5.Retrieving URL scan reports                          : --retrieving_url_scan_reports
6.Retrieving IP address reports (includes Passive DNS) : --retrieving_ip_address_reports
7.Retrieving domain reports (includes Passive DNS)     : --retrieving_domain_reports
8.Making comments on files and URLs                    : --making_comments_on_files_and_urls
###############################################################################################
'''

def Sending_and_scanning_files(sending_and_scanning_files):
	# print sending_and_scanning_files
	params = {'apikey' : API_KEY}
	files = {'file' : (sending_and_scanning_files, open(sending_and_scanning_files, 'rb'))}
	response = requests.post('https://www.virustotal.com/vtapi/v2/file/scan', files=files, params=params)
	if response.text != '':
		data = json.loads(response.text)
		Retrieving_file_scan_reports(str(data['sha256']))
	else:
		print 'Check Failed:('

def Rescanning_already_submitted_files(rescanning_already_submitted_files):
	params = {'apikey' : API_KEY, 'resource' : rescanning_already_submitted_files}
	headers = {
		"Accept-Encoding" : "gzip, deflate",
		"User-Agent" : "gzip,  My Python requests library example client or username"
	}
	response = requests.post('https://www.virustotal.com/vtapi/v2/file/rescan', params=params)
	if response.text != '':
		data = json.loads(response.text)
		Retrieving_file_scan_reports(str(data['sha256']))
	else:
		print 'Check Failed:('

def Retrieving_file_scan_reports(retrieving_file_scan_reports):
	params = {'apikey' : API_KEY, 'resource' : retrieving_file_scan_reports}
	headers = {
		"Accept-Encoding" : "gzip, deflate",
		"User-Agent" : "gzip,  My Python requests library example client or username"
	}
	response = requests.get('https://www.virustotal.com/vtapi/v2/file/report', params=params, headers=headers)
	if response.text != '':
		data = json.loads(response.text)
		print '#####################File Info##########################################'
		print 'Scan_ID       : ' + data['scan_id']
		print 'MD5           : ' + data['md5']
		print 'SHA1          : ' + data['sha1']
		print 'SHA256        : ' + data['sha256']
		print 'Resource      : ' + data['resource']
		print 'Response_Code : ' + str(data['response_code'])
		print 'Scan_Date     : ' + data['scan_date']
		print 'Permalink     : ' + data['permalink']
		print 'Verbose_Msg   : ' + data['verbose_msg']
		print 'Positives     : ' + str(data['positives'])
		print 'Total         : ' + str(data['total'])
		print '####################Scan Result#########################################'
		Keys = data['scans'].keys()
		for key in Keys:
			print str(key) + ' : ' + str(data['scans'][key]['result'])
		print '########################End#############################################'
	else:
		print 'Check Failed:('

def Sending_and_scanning_URLs(sending_and_scanning_urls):
	params = {'apikey' : API_KEY, 'url': sending_and_scanning_urls}
	response = requests.post('https://www.virustotal.com/vtapi/v2/url/scan', data=params)
	if response.text != '':
		data = json.loads(response.text)
		Retrieving_URL_scan_reports(data['url'])
	else:
		print 'Check Failed:('

def Retrieving_URL_scan_reports(retrieving_url_scan_reports):
	headers = {
		"Accept-Encoding" : "gzip, deflate",
		"User-Agent" : "gzip,  My Python requests library example client or username"
	}
	params = {'apikey' : API_KEY, 'resource' : retrieving_url_scan_reports}
	response = requests.post('https://www.virustotal.com/vtapi/v2/url/report', params=params, headers=headers)
	if response.text != '':
		data = json.loads(response.text)
		print '#####################Site Info##########################################'
		print 'Scan_ID       : ' + data['scan_id']
		print 'Resource      : ' + data['resource']
		print 'URL           : ' + data['url']
		print 'Response_Code : ' + str(data['response_code'])
		print 'Scan_Date     : ' + data['scan_date']
		print 'Permalink     : ' + data['permalink']
		print 'Verbose_Msg   : ' + data['verbose_msg']
		print 'Filescan_ID   : ' + str(data['filescan_id'])
		print 'Positives     : ' + str(data['positives'])
		print 'Total         : ' + str(data['total'])
		print '####################Scan Result#########################################'
		Keys = data['scans'].keys()
		for key in Keys:
			print str(key) + ' : ' + str(data['scans'][key]['result'])
		print '########################End#############################################'
	else:
		print 'Check Failed:('

def Retrieving_IP_address_reports(retrieving_ip_address_reports):
	params = {'ip' : retrieving_ip_address_reports, 'apikey' : API_KEY}
	response = requests.get('https://www.virustotal.com/vtapi/v2/ip-address/report', params=params)
	if response.text != '':
		data = json.loads(response.text)
		print '#####################IP Info############################################'
		print 'asn           :'  + data['asn']
		print 'country       : ' + data['country']
		print 'response_code : ' + str(data['response_code'])
		print 'as_owner      : ' + data['as_owner']
		print 'verbose_msg   : ' + data['verbose_msg']
		print '####################Scan Result#########################################'
		for detected_url in data['detected_urls']:
			print 'URL       : ' + detected_url['url']
			print 'Positives : ' + str(detected_url['positives'])
			print 'Total     : ' + str(detected_url['total'])
		print '########################End#############################################'
	else:
		print 'Check Failed:('

def Retrieving_domain_reports(retrieving_domain_reports):
	params = {'domain' : retrieving_domain_reports, 'apikey' : API_KEY}
	response = requests.get('https://www.virustotal.com/vtapi/v2/domain/report', params=params)
	if response.text != '':
		data = json.loads(response.text)
		print '#####################Domain Info########################################'
		print 'BitDefender category :'  + data['BitDefender category']
		print 'whois                : ' + data['whois']
		print 'whois_timestamp      : ' + str(data['whois_timestamp'])
		print 'verbose_msg          : ' + data['verbose_msg']
		print '####################Scan Result#########################################'
		for detected_url in data['detected_urls']:
			print 'URL        : ' + detected_url['url']
			print 'Positives  : ' + str(detected_url['positives'])
			print 'Total      : ' + str(detected_url['total'])
		for ip in data['resolutions']:
			print 'IP_Address : ' + ip['ip_address']
		for subdomain in data['subdomains']:
			print 'Subdomain           : ' + subdomain
		print '########################End#############################################'
	else:
		print 'Check Failed:('

def Making_comments_on_files_and_URLs(making_comments_on_files_and_urls):
	welcome()

def main():
	args = parse_arg()
	if args.sending_and_scanning_files:
		Sending_and_scanning_files(args.sending_and_scanning_files)
	elif args.rescanning_already_submitted_files:
		Rescanning_already_submitted_files(args.rescanning_already_submitted_files)
	elif args.retrieving_file_scan_reports:
		Retrieving_file_scan_reports(args.retrieving_file_scan_reports)
	elif args.sending_and_scanning_urls:
		Sending_and_scanning_URLs(args.sending_and_scanning_urls)
	elif args.retrieving_url_scan_reports:
		Retrieving_URL_scan_reports(args.retrieving_url_scan_reports)
	elif args.retrieving_ip_address_reports:
		Retrieving_IP_address_reports(args.retrieving_ip_address_reports)
	elif args.retrieving_domain_reports:
		Retrieving_domain_reports(args.retrieving_domain_reports)
	elif args.making_comments_on_files_and_urls:
		Making_comments_on_files_and_URLs(args.making_comments_on_files_and_urls)
	else:
		welcome()

if __name__ == '__main__':
	main()