#main file for CityEcho
import sqlite3

class Main():

    #gets data from report
    def PullData (already):
        #with open('input.txt') as f:
         #   i = 0
          #  for line in f:
           #     if line == 'None':
            #        return (None, None, None)
             #   if i == iter:
              #      issue = line
               # if i == 1+iter:
                #    street = line
                #if i == 2+iter:
                 #   status = line
                  #  break
                #i += 1

        #return (street, issue, status)

        conn = sqlite3.connect('../admin-ui/cityecho/db.sqlite3')
        i = 0
        line = conn.cursor.fetchall()
        while i < already:
            line = conn.cursor.fetchone()
            i += 1
        if line == None:
            return (None, None, None, None, None)
        st_num = line[0]
        st_name = line[1]
        city = line[2]
        state = line[3]
        issue = line[4]

        return (st_num, st_name, city, state, issue)

    #creates entry for report
    def addToDictionary(issue, possibleIssues):
        (cost, priority) = possibleIssues[issue]
        return (cost, priority, 1)

    #increments number of reports by 1 for duplicate report
    def incrimentReportNumber(reportedIssues, report):
        for key in reportedIssues.keys():
            if key == report:
                (cost, priority, number) = reportedIssues[key]
                return (cost, priority, number+1)

    #sorts new dictionary into priority database
    def prioratize(reportedIssues):
        final_data = dict()
        prioritizedList = []

        for street, issue in reportedIssues:
            (cost, priority, number) = reportedIssues[(street, issue)]
            final_priority = number**priority + cost
            final_data[final_priority] = (street, issue)

        while final_data:
            prioritizedList.append(final_data[max(final_data.iterkeys())])
            final_data.pop(max(final_data.iterkeys()), None)
        #print min(final_data[min(final_data.iterkeys())])

        return prioritizedList

    def priority_table(list):

        newTable = sqlite3.connect('new.sqlite3')

        newTable.execute('''CREATE TABLE ISSUES
                           (STREET NUMBER INT PRIMARY KEY     NOT NULL,
                           STREET NAME   TEXT    NOT NULL,
                           CITY          TEXT    NOT NULL,
                           STATE         TEXT,
                           ISSUE         TEXT);''')

        for item in list:
            newTable.execute("INSERT INTO ISSUES (STREET NUMBER,STREET NAME,CITY,STATE,ISSUE) \
                  VALUES (item[0], item[1], item[2], item[3], item[4])");

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
    (st_num, st_name, city, state, issue) = PullData(0)
    #print PullData(0)
    i = 0
    while not st_num == None:

        #Create new dictionary from submitted issues
        if reportedIssues.get((st_num, st_name, city, state, issue)) == None:
            reportedIssues[(st_num, st_name, city, state, issue)] = addToDictionary(issue, possibleIssues)
        else:
            #Increment number of reports by 1 if already reported
            reportedIssues[(st_num, st_name, city, state, issue)] = incrimentReportNumber(reportedIssues, (st_num, st_name, city, state, issue))

        i += 1
        (st_num, st_name, city, state, issue) = PullData(i)
        #print PullData(i)

    # Create official priority
    list = prioratize(reportedIssues)
    print list


