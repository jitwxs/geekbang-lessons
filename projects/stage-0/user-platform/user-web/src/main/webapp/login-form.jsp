<head>
<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
	<title>Register Form</title>
    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
</head>
<body>
	<div class="container">
		<form class="form-signin" method="post" action="/api/register">
			<h1 class="h3 mb-3 font-weight-normal">注册</h1>
			<label for="inputUsername" class="sr-only">Username</label>
			<input type="text" id="inputUsername" name="inputUsername" class="form-control" placeholder="请输入用户名" required autofocus>

			<label for="inputPassword" class="sr-only">Password</label>
			<input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="请输入密码" required>

			<label for="inputTel" class="sr-only">Username</label>
			<input type="text" id="inputTel" name="inputTel" class="form-control" placeholder="请输入手机号码" required autofocus>

			<label for="inputEmail" class="sr-only">Email</label>
			<input type="email" id="inputEmail" name="inputEmail" class="form-control" placeholder="请输入电子邮件" required autofocus>

			<div class="checkbox mb-3">
				<label> <input type="checkbox" value="remember-me">
					Remember me
				</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
			<p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
		</form>
	</div>
</body>