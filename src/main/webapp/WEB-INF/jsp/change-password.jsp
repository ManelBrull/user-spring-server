<%@include file="includes/header.jsp"%>

<div class="container">
	<div class="panel panel-default">

		<div class="panel-heading">
			<h3 class="panel-title">Change password</h3>
		</div>

		<div class="panel-body">

			<form:form modelAttribute="changePasswordForm" role="form">

				<form:errors />

				<div class="form-group">
					<form:label path="oldPassword">Type current password</form:label>
					<form:password path="oldPassword" class="form-control"
						placeholder="Current password" />
					<form:errors cssClass="error" path="oldPassword" />
				</div>

				<div class="form-group">
					<form:label path="newPassword">Type new password</form:label>
					<form:password path="newPassword" class="form-control"
						placeholder="New password" />
					<form:errors cssClass="error" path="newPassword" />
				</div>

				<div class="form-group">
					<form:label path="newRetypePassword">Retype new password</form:label>
					<form:password path="newRetypePassword" class="form-control"
						placeholder="Retype new password" />
					<form:errors cssClass="error" path="newRetypePassword" />
				</div>

				<button type="submit" class="btn btn-default">Submit</button>

			</form:form>
		</div>
	</div>
</div>
<%@include file="includes/footer.jsp"%>
