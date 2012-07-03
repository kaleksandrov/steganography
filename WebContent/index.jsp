<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	
	<head>
			
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/theme/img/favicon.jpg" />
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/theme/css/style.css" />
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/theme/css/sunny/jquery-ui-1.8.19.custom.css" />
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/theme/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/theme/js/jquery-ui-1.8.19.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/theme/js/jquery.form.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/theme/js/utils.js"></script>
		
		<title>Steganography</title>
	
	</head>
	
	<body>
		
		<div id="container">
	
			<div id="tabs">
			
				<ul>
					<li><a href="#tab-encode">Encode</a></li>
					<li><a href="#tab-decode">Decode</a></li>
				</ul>
				
				<div id="tab-encode">
				
					<form 
						id="form-encode"
						action="${pageContext.request.contextPath}/encode" 
						method="post" 
						enctype="multipart/form-data" 
						class="form">
						
						<div id="encode-messages" class="messages"></div>
						
						<div class="left half">
							
							<div class="header">File to encode</div>
							
							<div class="inner-container">
							
								<input 
									type="radio" 
									id="encode-radio-resource-local" 
									name="encode-source" 
									class="left radio" 
									value="local" 
									onchange="toggleEncodeResourceSelection()">
								<label 
									for="encode-radio-resource-local" 
									class="label">
									Local resource
								</label>
								<input 
									type="file" 
									id="file-encode-local" 
									name="file-encode-local" 
									class="input" 
									size="14" />
								<div class="clear"></div>
								
								<input 
									type="radio" 
									id="encode-radio-resource-remote" 
									name="encode-source" 
									class="left radio" 
									value="remote" 
									onchange="toggleEncodeResourceSelection()">
								<label 
									for="encode-radio-resource-remote" 
									class="label">
									Remote resource
								</label>
								<input 
									type="text" 
									id="file-encode-remote" 
									name="file-encode-remote" 
									class="input"/>
								<div class="clear"></div>
								
								<label
									for="input-encode-password" 
									style="margin-left: 20px;"
									class="label">
									Password
								</label>
								<input 
									type="password" 
									id="input-encode-password" 
									name="input-encode-password" 
									autocomplete="off"
									class="input"/>
								<div class="clear"></div>
								
								<input 
									type="submit" 
									id="button-encode" 
									value="Encode" 
									class="input" 
									onclick="startLoadingEncode();" />
								<div class="clear"></div>
							
							</div>
						
						</div>
						
						<div class="right half" >
						
							<div class="header">Message</div>
							
							<div class="inner-container">
							
								<textarea 
									id="hidden-message" 
									name="hidden-message" 
									class="textarea"
									maxlength="9999"
									rows="5" 
									cols="37">
									${requestScope.encodeMessage }
								</textarea>
								<div class="clear"></div>
						
							</div>
							
						</div>
						
						<div class="clear"></div>
						
					</form>
					
					<div class="clear"></div>
					
					<div id="images" class="images">
					
						<div class="left half">
						
							<div class="inner-container-left">
							
								<div class="header">Original image</div>			
								<div class="spacer"></div>
								<div class="clear"></div>		
								
								<a href="${pageContext.request.contextPath}/image?version=original" target="_blank"	>
								
									<img 
										id="image-original"
										src="${pageContext.request.contextPath}/image?version=original" 
										alt="original-image" 
										class="image right"/>
										
								</a>
								
							</div>
							
						</div>
						
						<div class="right half">
						
							<div class="inner-container-right">
							
								<div class="header">Encoded image</div>
								<div class="spacer"></div>
								<div class="clear"></div>
								
								<a href="${pageContext.request.contextPath}/image?version=encoded" target="_blank">
								
									<img 
										id="image-encoded" 
										src="${pageContext.request.contextPath}/image?version=encoded" 
										alt="encoded-image" 
										class="image left"/>
									
								</a>
								
							</div>
							
						</div>
						
						<div class="clear"></div>
						
					</div>
					
					<div id="loading-encode" class="loading"></div>
		
				</div>
				
				<div id="tab-decode">
				
					<form 
						id="form-decode"
						action="${pageContext.request.contextPath}/decode" 
						method="post" 
						enctype="multipart/form-data" 
						class="form">
						
						<div id="decode-messages" class="messages"></div>
						
						<div class="left half">
							
							<div class="header">File to decode</div>
							
							<div class="inner-container">
							
								<input 
									type="radio" 
									id="decode-radio-resource-local" 
									name="decode-source" 
									class="left radio" 
									value="local" 
									onchange="toggleDecodeResourceSelection()">
								<label 
									for="decode-radio-resource-local" 
									class="label">
									Local resource
								</label>
								<input 
									type="file" 
									id="file-decode-local" 
									name="file-decode-local" 
									class="input" 
									size="14"/>
								<div class="clear"></div>
								
								<input 
									type="radio" 
									id="decode-radio-resource-remote" 
									name="decode-source" 
									class="left radio" 
									value="remote" 
									onchange="toggleDecodeResourceSelection()">
								<label 
									for="decode-radio-resource-remote" 
									class="label">
									Remote resource
								</label>
								<input 
									type="text" 
									id="file-decode-remote" 
									name="file-decode-remote" 
									class="input"/>
								<div class="clear"></div>
								
								<label
									for="input-decode-password" 
									style="margin-left: 20px;"
									class="label">
									Password
								</label>
								<input 
									type="password" 
									id="input-decode-password" 
									name="input-decode-password" 
									autocomplete="off"
									class="input"/>
								<div class="clear"></div>
								
								<input 
									type="submit" 
									id="button-decode" 
									value="Decode" 
									class="input"
									onclick="startLoadingDecode();" />
								<div class="clear"></div>
							
							</div>
						
						</div>

						<div class="right half" >
						
							<div class="header">Message</div>
							
							<div class="inner-container">
							
								<textarea 
									id="decoded-message"
									rows="5" 
									cols="37" 
									readonly="readonly" 
									class="textarea">
									${requestScope.decodedMessage}
								</textarea>
								<div class="clear"></div>
						
							</div>
							
						</div>
						
					</form>
				
					<div class="clear"></div>
					
					<div id="loading-decode" class="loading"></div>
					
				</div>
			
			</div>
	
		</div>
	
	</body>
	
</html>