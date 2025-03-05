Feature: Employee Record Management
  As an HR manager,
  I want to manage employee records,
  So that I can ensure accurate employee information is maintained.

#  Background:
#    Given the employee management system is operational

  Scenario: Validate Creating New Employee Record
    Given that I have setup "employee" data
      | name  | John              |
      | age   | 35                |
      | title | Senior Consultant |
    And that I have setup employee recruitment data
    When I query the employee recruitment data
    Then I should be able receive confirmation of his employment
      | accounting[1].firstName       | John                     |
      | accounting[1].level           | Senior Consultant        |
      | accounting[1].salary.amount   | 200000                   |
      | accounting[1].salary.currency | rupees                   |
      | accounting[1].domain          | wealthAndAssetManagement |
      | accounting[1].salary.grade    | level 2                  |

  