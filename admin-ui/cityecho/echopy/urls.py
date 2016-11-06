from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.index, name="index"),
    url(r'^gcp$', views.get_city_probs, name="gcp"),
    url(r'^mr', views.make_report, name="mr"),
]
