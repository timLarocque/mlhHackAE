from django.shortcuts import render
import json
import urllib

# Create your views here.
from django.http import HttpResponse, JsonResponse, StreamingHttpResponse
from django.template import loader

from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.views.decorators.csrf import csrf_exempt
from django.forms.models import model_to_dict
from .models import Report


def index(request):
    template = loader.get_template("echopy/index.html")
    print(staticfiles_urlpatterns())
    return HttpResponse(template.render({}, request))

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
        return JsonResponse({'result': 'ok'})
