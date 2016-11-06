from __future__ import unicode_literals

from django.db import models
#from django.auth.models import User
# Create your models here.
class Issue(models.Model):
    def __str__(self):
        return "{0} {1}".Format(self.street_num, self.street_name)
    street_num = models.IntegerField()
    street_name = models.CharField(max_length=150)
    city = models.CharField(max_length=150)
    state = models.CharField(max_length=150)
    issueType = models.CharField(max_length=50)
    num_reports = models.IntegerField()

class Report(models.Model):
    def __str__(self):
        return "{0} {1}".Format(self.street_num, self.street_name)
    street_num = models.IntegerField()
    street_name = models.CharField(max_length=150)
    city = models.CharField(max_length=150)
    state = models.CharField(max_length=150)
    issueType = models.CharField(max_length=50)
    email = models.CharField(max_length=150)
