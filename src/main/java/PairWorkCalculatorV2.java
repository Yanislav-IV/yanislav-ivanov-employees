import emp.Employee;
import utils.DateParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

import static java.time.temporal.ChronoUnit.DAYS;


public class PairWorkCalculatorV2 {
    public static void main(String[] args) throws IOException {
        final DateParser dateParser = new DateParser("/datePatterns.txt");
        Map<Integer, List<Employee>> projects = readEmployeesToList(dateParser, "/employees.csv");
        int emp1Id = -1;
        int emp2Id = -1;
        int projId = -1;
        int mostDaysWorked = 0;

        for (var entry : projects.entrySet()) {
            List<Employee> employeeList = entry.getValue();
            employeeList.sort(Comparator.comparing(Employee::getDateFrom));
            for (int i = 0; i < employeeList.size(); i++) {
                Employee emp1 = employeeList.get(i);
                for (int j = i + 1; j < employeeList.size(); j++) {
                    Employee emp2 = employeeList.get(j);

                    // no teamwork
                    if (emp1.getDateTo().isBefore(emp2.getDateFrom())) {
                        continue;
                    }

                    LocalDate lastDayTeamwork = emp1.getDateTo().isBefore(emp2.getDateTo()) ? emp1.getDateTo() : emp2.getDateTo();
                    int daysTeamWork = (int) (DAYS.between(emp2.getDateFrom(), lastDayTeamwork));

                    if (daysTeamWork > mostDaysWorked) {
                        emp1Id = emp1.getId();
                        emp2Id = emp2.getId();
                        projId = entry.getKey();
                        mostDaysWorked = daysTeamWork;
                    }
                }
            }
        }

        System.out.printf("Employee %d, Employee %d, Project %d, Days worked: %d",
                emp1Id, emp2Id, projId, mostDaysWorked);
    }


    private static Map<Integer, List<Employee>> readEmployeesToList(DateParser dateParser, String employeesFileName) throws IOException {
        final BufferedReader rd = new BufferedReader(new InputStreamReader(
                PairWorkCalculatorV2.class.getResourceAsStream(employeesFileName)));

        String line;
        Map<Integer, List<Employee>> projects = new HashMap<>();

        while ((line = rd.readLine()) != null) {
            String[] params = line.split(", ");

            Integer id = Integer.parseInt(params[0]);
            Integer projectId = Integer.parseInt(params[1]);
            LocalDate fromDate;
            LocalDate toDate;
            try {
                fromDate = dateParser.parseDate(params[2], false);
                toDate = dateParser.parseDate(params[3], false);
            } catch (Exception e) {
                fromDate = dateParser.parseDate(params[2], true);
                toDate = dateParser.parseDate(params[3], true);
            }

            Employee emp = new Employee(id, fromDate, toDate);
            projects.putIfAbsent(projectId, new ArrayList<>());
            projects.get(projectId).add(emp);
        }

        return projects;
    }
}
