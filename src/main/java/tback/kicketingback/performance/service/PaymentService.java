package tback.kicketingback.performance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.dto.PaymentRequest;
import tback.kicketingback.performance.exception.exceptions.InvalidPayCancelRequestException;
import tback.kicketingback.performance.exception.exceptions.InvalidPayRequestException;
import tback.kicketingback.performance.exception.exceptions.PaymentCancelServerErrorException;
import tback.kicketingback.performance.exception.exceptions.PaymentServerErrorException;

@Service
@RequiredArgsConstructor
public class PaymentService {

	@Value("${payments.custom.verify-pay-url}")
	private String payUrl;
	@Value("${payments.custom.cancel-pay-url}")
	private String payCancelUrl;

	private final RestTemplate restTemplate;

	public void verifyPayment(String orderNumber) {
		HttpHeaders headers = createHeaders();
		PaymentRequest paymentRequest = new PaymentRequest(orderNumber);

		HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);

		try {
			restTemplate.postForEntity(payUrl, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			handlePaymentVerifyException(e);
		}
	}

	public void cancelPayment(String orderNumber) {
		HttpHeaders headers = createHeaders();
		PaymentRequest paymentRequest = new PaymentRequest(orderNumber);

		HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);

		try {
			restTemplate.postForEntity(payCancelUrl, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			handlePaymentCancelException(e);
		}
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private void handlePaymentVerifyException(HttpClientErrorException e) {
		if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
			throw new InvalidPayRequestException();
		} else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new PaymentServerErrorException();
		}
	}

	private void handlePaymentCancelException(HttpClientErrorException e) {
		if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
			throw new InvalidPayCancelRequestException();
		} else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new PaymentCancelServerErrorException();
		}
	}
}
