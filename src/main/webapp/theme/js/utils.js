$(document).ready(
		function() {
			// Create jQueryUI tabs
			createTabs();

			// Attach ajax form submition
			bindFormForAjax('form-encode', stopLoadingEncode, updateImages,
					null);
			bindFormForAjax('form-decode', stopLoadingDecode,
					updateResultMessage, null);

			// Set default values
			$('#encode-radio-resource-local').attr('checked', 'checked');
			$('#file-encode-remote').attr('disabled', 'disabled');
			$('#hidden-message').val('');

			$('#decode-radio-resource-local').attr('checked', 'checked');
			$('#file-decode-remote').attr('disabled', 'disabled');
			$('#decoded-message').val('');
		});

$.urlParam = function(name) {
	var results = new RegExp('[\\?&]' + name + '=([^&#]*)')
			.exec(window.location.href);
	if (!results) {
		return 0;
	}
	return results[1] || 0;
};

function createButtons() {
	$('#button-encode').button();
	$('#button-decode').button();
}

function bindFormForAjax(formId, completeCallback, successCallback,
		errorCallback) {
	$('#' + formId).ajaxForm({
		dataType : 'json',
		success : function(data) {
			if (null != successCallback) {
				successCallback(data);
			}
		},
		error : function(data) {
			if (null != errorCallback) {
				errorCallback(data);
			}
		},
		complete : function(data) {
			if (null != completeCallback) {
				completeCallback(data);
			}
		}
	});
}

function updateResultMessage(data) {
	console.log(data);
	console.log(data.status.success);

	jDecodeMessage = $('#decoded-message');
	if (true == data.status.success) {
		console.log(data.message);
		jDecodeMessage.val(data.message);
	} else {
		jDecodeMessage.val('');
	}

	var messagesList = '';
	if (data.status.messages.length > 0) {
		messagesList = '<ul>';
		$.each(data.status.messages, function(index, message) {
			messagesList += '<li class="' + message.type + '">' + message.text
					+ '</li>';
		});
		messagesList += '</ul>';
	}

	$('#decode-messages').html(messagesList);
}

function updateImages(data) {
	console.log(data);

	if (true == data.status.success) {
		var originalImage = $("#image-original");
		var encodedImage = $("#image-encoded");

		d = new Date();

		var originalSrc = originalImage.attr('src') + '&' + d.getTime();
		var encodedSrc = encodedImage.attr('src') + '&' + d.getTime();

		originalImage.attr('src', '');
		encodedImage.attr('src', '');

		$('#images').show();

		originalImage.attr("src", originalSrc);
		encodedImage.attr("src", encodedSrc);
	}

	var messagesList = '';
	if (data.status.messages.length > 0) {
		messagesList = '<ul>';
		$.each(data.status.messages, function(index, message) {
			messagesList += '<li class="' + message.type + '">' + message.text
					+ '</li>';
		});
		messagesList += '</ul>';
	}

	$('#encode-messages').html(messagesList);
}

function createTabs() {
	$('#tabs').tabs();
}

function startLoadingEncode() {
	var loading = $('#loading-encode');
	var height = $('#tabs').height();
	loading.height(height);
	loading.show();
}

function startLoadingDecode() {
	var loading = $('#loading-decode');
	var height = $('#tabs').height();
	loading.height(height);
	loading.show();
}

function stopLoadingEncode() {
	$('#loading-encode').hide();
}

function stopLoadingDecode() {
	$('#loading-decode').hide();
}

function toggleEncodeResourceSelection() {
	var radioLocal = $('#encode-radio-resource-local');
	var radioRemote = $('#encode-radio-resource-remote');
	var inputLocal = $('#file-encode-local');
	var inputRemote = $('#file-encode-remote');

	if ('checked' == radioLocal.attr('checked')) {
		inputLocal.removeAttr('disabled');
		inputRemote.attr('disabled', 'disabled');
		inputRemote.val('');
	}
	if ('checked' == radioRemote.attr('checked')) {
		inputLocal.attr('disabled', 'disabled');
		inputLocal.val('');
		inputRemote.removeAttr('disabled');
	}
}

function toggleDecodeResourceSelection() {
	var radioLocal = $('#decode-radio-resource-local');
	var radioRemote = $('#decode-radio-resource-remote');
	var inputLocal = $('#file-decode-local');
	var inputRemote = $('#file-decode-remote');

	if ('checked' == radioLocal.attr('checked')) {
		inputLocal.removeAttr('disabled');
		inputRemote.attr('disabled', 'disabled');
		inputRemote.val('');
	}
	if ('checked' == radioRemote.attr('checked')) {
		inputLocal.attr('disabled', 'disabled');
		inputLocal.val('');
		inputRemote.removeAttr('disabled');
	}
}
