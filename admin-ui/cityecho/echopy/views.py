from django.shortcuts import render
import json
import urllib
from Main import *

# Create your views here.
from django.http import HttpResponse, JsonResponse, StreamingHttpResponse
from django.template import loader

from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.views.decorators.csrf import csrf_exempt
from django.forms.models import model_to_dict
from .models import Report
from .models import Issue

def index(request):
    template = loader.get_template("echopy/index.html")
    issues = Issue.objects.all()
    return HttpResponse(template.render({'issues': issues}, request))

def get_issues(request):
    all_issues = Issue.objects.all()
    res = {'result' : []}
    for x in all_issues:
        tempd = model_to_dict(x)
        res['result'].append(tempd)
    return JsonResponse(res)

def remove_issue(request):
    to_del = request.META['QUERY_STRING'].split('&')
    ls = list()
    for x in to_del:
        y = x.split('=')
        ls.append((y[0], urllib.unquote(y[1]).rstrip()))
    d = dict(ls)
    d['street_num'] = int(d['street_num'])
    r = Report.objects.all().filter(street_num=d['street_num'],
                                    street_name=d['street_name'],
                                    city=d['city'],
                                    state=d['state'],
                                    issueType=d['issueType'])
    r.delete()
    i = Issue.objects.all().filter(street_num=d['street_num'],
                                    street_name=d['street_name'],
                                    city=d['city'],
                                    state=d['state'],
                                    issueType=d['issueType'])
    i.delete()
    return index(request)

def get_city_probs(data):
    all_reports = Report.objects.all()
    res = {'result': []}
    for x in all_reports:
        tempd = model_to_dict(x)
        res['result'].append(tempd)
    return JsonResponse(res)

def make_report(request):
    if request.method == 'GET':
        print request.META['QUERY_STRING']
        the_data = request.META['QUERY_STRING'].split('&')
        ls = list()
        for x in the_data:
            y = x.split('=')
            ls.append((y[0], urllib.unquote(y[1]).rstrip()))
        d = dict(ls)
        snum = int(d['street_num'])
        sname = d['street_name']
        c = d['city']
        st = d['state']
        iType = d['type']
        e = d['email']
        rep = Report(street_num=snum,
                     street_name=sname,
                     city=c,
                     state=st,
                     issueType=iType,
                     email=e)
        rep.save()
        res = get_city_probs(request.META['QUERY_STRING'])
        new_main = Main(res.content)
        new_issues = new_main.sort_issues()
        all_issues = Issue.objects.all()
        all_issues.delete()
        for x in new_issues:
            print x
            snum = int(x['street_num'])
            sname = x['street_name']
            c = x['city']
            st = x['state']
            iType = x['issueType']
            nr = x['num_reports']
            i = Issue(street_num=snum,
                      street_name=sname,
                      city=c,
                      state=st,
                      issueType=iType,
                      num_reports=nr)
            i.save()
        return JsonResponse({'result': 'ok'})
