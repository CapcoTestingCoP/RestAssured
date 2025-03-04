Feature: Test to Validate Creating New Employee Record

  Scenario: Validate Creating New Employee Record
    Given that I have setup "employee" data
      | name  | John          |
      | age   | 35            |
      | title | IT Consultant |
    And that I have setup employee recruitment data
    When I query the employee recruitment data
    Then I should be able receive confirmation of his employment
      | accounting, firstName                                  | John          |
      | accounting, workInfo, title                            | IT Consultant |
      | accounting, salary, amount                             | £33000        |
      | accounting, workInfo, subDepart, report, reportManager | James Will    |
      | accounting[0].workInfo.subDepart.report.reportManager  | James Will    |