<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="${RequestURI }" modelAttribute="configurationParameters">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:label path="sysName">
		<spring:message code="configurationParameters.sysName" />:
	</form:label>
	<form:input path="sysName"/>
	<form:errors cssClass="error" path="sysName" />
	<br>
	<br>
	
	
	<form:label path="welcomeMessageEsp">
		<spring:message code="configurationParameters.welcomeMessageEsp" />:
	</form:label>
	<form:input path="welcomeMessageEsp" />
	<form:errors cssClass="error" path="welcomeMessageEsp" />
	<br>
	<br>
	
	<form:label path="welcomeMessageEn">
		<spring:message code="configurationParameters.welcomeMessageEn" />:
	</form:label>
	<form:input path="welcomeMessageEn"/>
	<form:errors cssClass="error" path="welcomeMessageEn" />
	<br>
	<br>
	
	<form:label path="maxFinderResults">
		<spring:message code="configurationParameters.maxFinderResults" />:
	</form:label>
	<form:input path="maxFinderResults"/>
	<form:errors cssClass="error" path="maxFinderResults" />
	<br>
	<br>
	
	<form:label path="finderTime">
		<spring:message code="configurationParameters.finderTime" />:
	</form:label>
	<form:input path="finderTime"/>
	<form:errors cssClass="error" path="finderTime" />
	<br>
	<br>
	
	<form:label path="banner">
		<spring:message code="configurationParameters.banner" />:
	</form:label>
	<form:input path="banner" placeholder="http://" />
	<form:errors cssClass="error" path="banner" />
	<br>
	<br>

	<form:label path="spamWords">
		<spring:message code="configurationParameters.spamWords" />:
	</form:label>
	<br>
	<form:textarea path="spamWords"/>
	<form:errors cssClass="error" path="spamWords" />
	<br>
	<br>
	
	<form:label path="subjectLevels">
		<spring:message code="configurationParameters.subjectLevels" />:
	</form:label>
	<br>
	<form:textarea path="subjectLevels"/>
	<form:errors cssClass="error" path="subjectLevels" />
	<br>
	<br>
	
	<form:label path="countryPhoneCode">
		<spring:message code="configurationParameters.countryPhoneCode" />:
	</form:label>
	<form:input path="countryPhoneCode"/>
	<form:errors cssClass="error" path="countryPhoneCode" />
	<br>
	<br>
	
	<form:label path="creditCardMake">
		<spring:message code="configurationParameters.creditCardMake" />:
	</form:label>
	<br>
	<form:textarea path="creditCardMake"/>
	<form:errors cssClass="error" path="creditCardMake" />
	<br>
	<br>
	
	<form:label path="vat">
		<spring:message code="configurationParameters.vat" />:
	</form:label>
	<br>
	<form:textarea path="vat"/>
	<form:errors cssClass="error" path="vat" />
	<br>
	<br>
	
	<input type="submit" name="save"
		value="<spring:message code="configurationParameters.save" />" />&nbsp; 
	
	<input type="button" name="cancel"
		value="<spring:message code="configurationParameters.cancel" />"
		onclick="javascript:  window.location.replace('welcome/index.do');" />
	<br>
</form:form>