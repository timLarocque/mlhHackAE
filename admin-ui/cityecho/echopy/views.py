from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse, JsonResponse
from django.template import loader

from django.contrib.staticfiles.urls import staticfiles_urlpatterns


def index(request):
    template = loader.get_template("echopy/index.html")
    print(staticfiles_urlpatterns())
    return HttpResponse(template.render({}, request))

def get_city_probs(data):

    return JsonResponse({'result': [{'street_num': 2,
                            'street_name': 'University Ave',
                            'city': 'Lowell',
                            'state': 'MA',
                            'type': 'pothole'},
                            {'street_num': 645,
                            'street_name': 'Riverside St',
                            'city': 'Lowell',
                            'state': 'MA',
                            'type': 'road debris'}
                            ]})
