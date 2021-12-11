import emp.Employee;
import utils.DateParser;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

import static java.time.temporal.ChronoUnit.DAYS;


public class PairWorkCalculator {
    public static void main(String[] args) throws IOException {
        final DateParser dateParser = new DateParser("/datePatterns.txt");
        Map<Integer, List<Employee>> projects = readEmployeesToList(dateParser, "/employees.csv");
        Map<String, Integer> pairsMap = new HashMap<>();

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

                    int minId = Math.min(emp1.getId(), emp2.getId());
                    int maxId = Math.max(emp1.getId(), emp2.getId());

                    String pairId = new StringBuilder()
                            .append(minId)
                            .append("x")
                            .append(maxId)
                            .toString();

                    pairsMap.putIfAbsent(pairId, 0);
                    pairsMap.put(pairId, pairsMap.get(pairId) + daysTeamWork);
                }
            }
        }

        Entry<String, Integer> entry = pairsMap.entrySet().stream()
                .min(Entry.comparingByValue(Comparator.reverseOrder()))
                .orElse(null);

        String[] params = entry.getKey().split("x");

        System.out.printf("Employee ID %s, Employee ID %s, Days worked together in multiple projects: %d",
                params[0], params[1], entry.getValue());


    }


    private static Map<Integer, List<Employee>> readEmployeesToList(DateParser dateParser, String employeesFileName) throws IOException {
        final BufferedReader rd = new BufferedReader(new InputStreamReader(
                PairWorkCalculator.class.getResourceAsStream(employeesFileName)));

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
