package jdbc_servlets.repository;

import jdbc_servlets.config.DatabaseManagerConnector;
import jdbc_servlets.model.dao.ProjectDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements CRUDRepository<ProjectDao> {

    private final DatabaseManagerConnector connector;
    private static final String INSERT = "INSERT INTO projects(project_name, country, company_id, customer_id, " +
            "cost, date_of_creation) VALUES(?,?,?,?,?,?)";
    private static final String ALL_PROJECTS = "SELECT id, project_name, country, company_id, customer_id, cost, " +
            "date_of_creation FROM projects";
    private static final String UPDATE = "UPDATE projects SET project_name = ?, country = ?, cost = ?, company_id = ?, " +
            "customer_id = ?, date_of_creation = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM projects WHERE id = ?";
    private static final String PROJECT_BY_ID = "SELECT id, project_name, country, company_id, customer_id, cost, " +
            "date_of_creation FROM projects WHERE id = ?";
    private static final String SALARY_BY_PROJECT = "SELECT SUM(salary) AS salary FROM developers " +
            "JOIN developers_projects ON developers.id = developers_projects.developer_id " +
            "JOIN projects ON projects.id = developers_projects.project_id WHERE projects.project_name = ?";
    private static final String LIST_OF_PROJECTS = "SELECT projects.date_of_creation, projects.project_name, " +
            "COUNT(*) FROM developers_projects INNER JOIN developers " +
            "ON developers.id = developers_projects.developer_id INNER JOIN projects " +
            "ON projects.id = developers_projects.project_id GROUP BY projects.date_of_creation, projects.project_name;";

    public ProjectRepository(DatabaseManagerConnector connector) {
        this.connector = connector;
    }

    @Override
    public ProjectDao create(ProjectDao entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getProjectName());
            statement.setString(2, entity.getCountry());
            statement.setInt(3, entity.getCompanyId());
            statement.setInt(4, entity.getCustomerId());
            statement.setInt(5, entity.getCost());
            statement.setDate(6, entity.getDateOfCreation());
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return entity;
    }

    @Override
    public List<ProjectDao> findAll() {
        List<ProjectDao> projectDaoList = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_PROJECTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ProjectDao projectDao = new ProjectDao();
                projectDao.setId(resultSet.getInt("id"));
                projectDao.setProjectName(resultSet.getString("projectName"));
                projectDao.setCountry(resultSet.getString("country"));
                projectDao.setCompanyId(resultSet.getInt("companyId"));
                projectDao.setCustomerId(resultSet.getInt("customerId"));
                projectDao.setCost(resultSet.getInt("cost"));
                projectDao.setDateOfCreation(resultSet.getDate("dateOfCreation"));
                projectDaoList.add(projectDao);

            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return projectDaoList;
    }

    @Override
    public ProjectDao getByID(Integer id) {
        ProjectDao projectDao = new ProjectDao();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(PROJECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projectDao.setId(resultSet.getInt(1));
                projectDao.setProjectName(resultSet.getString(2));
                projectDao.setCountry(resultSet.getString(3));
                projectDao.setCompanyId(resultSet.getInt(4));
                projectDao.setCustomerId(resultSet.getInt(5));
                projectDao.setCost(resultSet.getInt(6));
                projectDao.setDateOfCreation(resultSet.getDate(7));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return projectDao;
    }

    public Integer salaryByProject(String projectName) {
        int sum = 0;
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SALARY_BY_PROJECT)) {
            statement.setString(1, projectName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sum = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public List<List<String>> listOfProjects() {
        List<List<String>> projectsList = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(LIST_OF_PROJECTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<String> listOfDao = new ArrayList<>();
                listOfDao.add(resultSet.getString("date_of_creation"));
                listOfDao.add(resultSet.getString("project_name"));
                listOfDao.add(resultSet.getString("count"));
                projectsList.add(listOfDao);
                            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectsList;
    }

    @Override
    public ProjectDao update(ProjectDao entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, entity.getProjectName());
            preparedStatement.setString(2, entity.getCountry());
            preparedStatement.setInt(3, entity.getCost());
            preparedStatement.setInt(4, entity.getCompanyId());
            preparedStatement.setInt(5, entity.getCustomerId());
            preparedStatement.setDate(6, entity.getDateOfCreation());
            preparedStatement.setInt(2, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return entity;
    }

    @Override
    public void delete(ProjectDao entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}

