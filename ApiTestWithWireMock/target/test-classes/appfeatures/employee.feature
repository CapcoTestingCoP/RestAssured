@API
Feature: Employee Record Management
  As an HR manager
  I want to manage employee records
  So that I can ensure accurate employee information is maintained

  Scenario Outline: Create a new employee record
    Given employee data below
      | Name         | <Name>         |
      | Level        | <Level>        |
      | Domain       | <Domain>       |
      | Capability   | <Capability>   |
      | PrimarySkill | <PrimarySkill> |
      | Grade        | <Grade>        |
      | Salary       | <Salary>       |
      | Currency     | <Currency>     |
    When the employee data is submitted
    Then a new employee record is created
    And the system displays a confirmation message "Employee record created successfully.
      | <Id>.Name         | <Name>         |
      | <Id>.Level        | <Level>        |
      | <Id>.Domain       | <Domain>       |
      | <Id>.Capability   | <Capability>   |
      | <Id>.PrimarySkill | <PrimarySkill> |
      | <Id>.Grade        | <Grade>        |
      | <Id>.Salary       | <Salary>       |
      | <Id>.Currency     | <Currency>     |

    Examples:
      | Id            | Name          | Level            | Domain                   | Capability         | PrimarySkill           | Grade | Salary | Currency |
      | accounting[0] | AliceWong     | HRSpecialist     | CorporateServices        | HR                 | Recruitment            | 1     | 85000  | USD      |
      | accounting[1] | VijaySingh    | SeniorConsultant | WealthAndAssetManagement | TechAndEngineering | QATestingAndAutomation | 2     | 200000 | INR      |
      | accounting[2] | ChijiokeAmago | SeniorConsultant | CrossDomain              | TechAndEngineering | QATestingAndAutomation | 1     | 150000 | GBP      |