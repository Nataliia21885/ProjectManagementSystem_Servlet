<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
  <head>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  </head>
    <body>
      <c:import url="/WEB-INF/jsp/navigation.jsp"/>
        <form action="/skills/update">
                    <label for="id"> Skill id(use only digits): </label><br>
                    <input type="text" id="id" name="id"><br>
                    <label for="name"> Language: </label><br>
                    <input type="text" id="language" name="language"><br>
                    <label for="hrm"> Level: </label><br>
                    <input type="text" id="level" name="level"><br>
                    <button type="submit">Update</button>
        </form>
        </br>
        <c:if test="${not empty updatedSkill}">
        <table>
            <thead>
                     <tr>
                        <td>Language:</td>
                        <td>Level:</td>
                     </tr>
            </thead>
           <tbody>
                       <tr>
                           <td>
                               <c:out value="${updatedSkill.language}"/>
                           </td>
                           <td>
                               <c:out value="${updatedSkill.level}"/>
                           </td>
                       </tr>
           </tbody>
        </table>
        <p>Skill is updated successfully</p>
         </c:if>
          <c:if test="${empty updatedSkill}">
             <p>Skill is not updated (fields are empty or entering id does not exist)</p>
          </c:if>
    </body>
  </html>