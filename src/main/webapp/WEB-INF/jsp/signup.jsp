<%@include file="includes/header.jsp"%>	
<div class="container">
<div class="panel panel-default">

	<div class="panel-heading">
		<h3 class="panel-title">Please signup</h3>
	</div>
	
	<div class="panel-body">
	
		<form:form modelAttribute="signupForm" role="form">
		
			<div class="form-group">
				<form:label path="email">Email address</form:label>
				<form:input path="email" type="email" class="form-control" placeholder="Enter email" />
				<form:errors cssClass="errors" path="email"/>
				<p class="help-block">Enter a unique email address. It will also be your login id.</p>
			</div>
			
			<div class="form-group">
				<form:label path="username">Name</form:label>
				<form:input path="username" class="form-control" placeholder="Enter username" />
				<form:errors cssClass="errors" path="username"/>
				<p class="help-block">Enter your username.</p>
			</div>
			
			<div class="form-group">
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Password" />
				<form:errors cssClass="errors" path="password"/>
				<p class="help-block">Use a strong password, please.</p>
			</div>
			
			<button type="submit" class="btn btn-default">Submit</button>
			
		</form:form>
	</div>
</div>
</div>
<%@include file="includes/footer.jsp"%>