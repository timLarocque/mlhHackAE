#main file for CityEcho
import json

class Main:
    def __init__(self, list):
        self.unsorted = json.loads(list)['result']

    #gets data from report
    def PullData (self, already, unsorted):

        if already == len(unsorted):
            return (None, None, None, None, None)

        st_num = unsorted[already]['street_num']
        st_name = unsorted[already]['street_name']
        city = unsorted[already]['city']
        state = unsorted[already]['state']
        issue = unsorted[already]['issueType']

        return (st_num, st_name, city, state, issue)

    #creates entry for report
    def addToDictionary(self, issue, possibleIssues):
        (cost, priority) = possibleIssues[issue]
        return (cost, priority, 1)

    #increments number of reports by 1 for duplicate report
    def incrimentReportNumber(self, reportedIssues, report):
        for key in reportedIssues.keys():
            if key == report:
                (cost, priority, number) = reportedIssues[key]
                return (cost, priority, number+1)

    #sorts new dictionary into priority database
    def prioratize(self, reportedIssues):
        final_data = dict()
        nextItem = dict()
        prioritizedList = []

        for st_num, st_name, city, state, issue in reportedIssues:
            (cost, priority, number) = reportedIssues[(st_num, st_name, city, state, issue)]
            final_priority = number**priority + cost
            final_data[(st_num, st_name, city, state, issue, number)] = final_priority

        while any(final_data):
            max_val = 0
            for st_num, st_name, city, state, issue, number in final_data:
                if final_data[(st_num, st_name, city, state, issue, number)] > max_val:
                    new_st_num = st_num
                    new_st_name = st_name
                    new_city = city
                    new_state = state
                    new_issue = issue
                    new_number = number
                    max_val = final_data[(st_num, st_name, city, state, issue, number)]
            nextItem['street_num'] = new_st_num
            nextItem['street_name'] = new_st_name
            nextItem['city'] = new_city
            nextItem['state'] = new_state
            nextItem['issueType'] = new_issue
            nextItem['num_reports'] = new_number
            print st_name

            prioritizedList.append(nextItem)
            del final_data[(new_st_num, new_st_name, new_city, new_state, new_issue, new_number)]
            nextItem = dict()

        return prioritizedList

    def sort_issues(self):
        #Dictionary of all possible problems as key and their cost and base priority
        possibleIssues = dict([('Pothole', (50, 1)),
              ('Non-Functional Traffic Light', (250000, 6)),
              ('Snow or Ice', (500, 9)),
              ('Backed Up Storm Drain', (500, 5)),
              ('Debris Obstrucing Road', (50, 5)),
              ('Telephone Pole Downed', (5000, 10))
              ])
        reportedIssues = dict()

        #Pull data from forms
        (st_num, st_name, city, state, issue) = self.PullData(0, self.unsorted)
        i = 0
        while not st_num == None:

            #Create new dictionary from submitted issues
            if not (st_num, st_name, city, state, issue) in reportedIssues:
                reportedIssues[(st_num, st_name, city, state, issue)] = self.addToDictionary(issue, possibleIssues)
            else:
                #Increment number of reports by 1 if already reported
                reportedIssues[(st_num, st_name, city, state, issue)] = self.incrimentReportNumber(reportedIssues, (st_num, st_name, city, state, issue))

            i += 1
            (st_num, st_name, city, state, issue) = self.PullData(i, self.unsorted)

        # Create official priority
        return self.prioratize(reportedIssues)
