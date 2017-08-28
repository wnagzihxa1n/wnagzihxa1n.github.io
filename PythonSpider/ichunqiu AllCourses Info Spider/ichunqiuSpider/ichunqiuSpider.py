#coding:utf-8

import sys
import json
import requests

source_url = 'http://www.ichunqiu.com/courses/ajaxCourses'


def function(url, index):
    headers = {
            'Host': 'www.ichunqiu.com',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0',
            'Accept': 'application/json, text/javascript, */*; q=0.01',
            'Accept-Language': 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
            'Accept-Encoding': 'gzip, deflate',
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'X-Requested-With': 'XMLHttpRequest',
            'Referer': 'http://www.ichunqiu.com/courses',
    }

    data = {
        'IsExp': '',
        'courseDiffcuty': '',
        'courseTag': '',
        'orderDirection': '',
        'orderField': '',
        'pageIndex': index,
        'producerId': '',
        'tagType': 2
    }
    json_source = requests.post(url=url, data=data, headers=headers)
    data = json.loads(json_source.text)
    result_len = len(data['course']['result'])
    for i in range(result_len):
        print "createTime:", data['course']['result'][i]['createTime'],
        # print "courseID:", data['course']['result'][i]['courseID'],
        print "producerName:", data['course']['result'][i]['producerName'],
        print "courseName:", data['course']['result'][i]['courseName']


def main():
    reload(sys)
    sys.setdefaultencoding('utf-8')
    for i in range(1, 12):
        function(source_url, i)


if __name__ == '__main__':
    main()


















































