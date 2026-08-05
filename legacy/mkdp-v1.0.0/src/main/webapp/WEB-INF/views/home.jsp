<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>MKDP</title>
</head>
<style>
h1, h2, h3, h4 {
	text-align: center;
	border-bottom: 10%;
	border-bottom: solid navy;
	margin: 0%;
	padding: 2%;
}

.mkdpConteiner {
	text-align: left;
	border-bottom: 10%;
	border-bottom: solid navy;
	margin: 0%;
	padding: 2%;
}

#grid {
	display: grid;
	grid-template-columns: 0.5fr 2fr;
}

#grid ol {
	border-right: 0.5%;
	border-right: solid navy;
	width: 90%;
	margin: 0%;
	padding-left: 10%;
}

#grid #article {
	padding-left: 2%;
}
</style>
<body>
	<h1>Market Data Provider Site</h1>
	<div class="mkdpConteiner">
		Real-time Market Data at Your Fingertips <br> The time on the
		server is ${serverTime}.
	</div>
	<div id="grid">
		<ol>
			<div style="font-weight: bold;">Page List</div>
			<li><a href="setCookie">setcookie</a></li>
			<li><a href="getCookie">getcookie</a></li>
			<li><a href="getHeader">getHeader</a></li>
			<li><a href="redirect">redirect</a></li>
			<li><a href="forward">forward</a></li>
			<li><a href="apiCall01">기업개황 apiCall</a></li>
			<div style="font-weight: bold;">Our Web Sites</div>
			<li><a
				href="https://mkdp.atlassian.net/wiki/spaces/MKDP/overview?homepageId=229378">Confluence</a></li>
			<li><a href="https://github.com/wnstjqaodls/MKDP">gitHub</a></li>
		</ol>
		<div id="article">
			<div style="font-weight: bold;">Contents</div>
			<p>The asset market is a complex and dynamic ecosystem that
				comprises of various financial instruments such as stocks, bonds,
				currencies, and commodities. The performance of these assets is
				closely monitored by investors, traders, and analysts alike as they
				seek to make informed decisions on buying, selling or holding these
				instruments. The market is constantly influenced by various factors
				such as economic indicators, geopolitical events, and investor
				sentiment, making it highly unpredictable and volatile. As a result,
				it is imperative to have access to reliable and up-to-date data to
				analyze and understand the trends and movements of the market. In
				today's fast-paced world, the ability to access real-time market
				data is crucial for investors and traders to stay ahead of the
				competition. With the advent of technology, the market data is
				readily available through various platforms such as financial news
				websites, market data providers, and mobile applications. Analyzing
				and interpreting the market data requires a strong understanding of
				financial principles and technical analysis. Traders and investors
				often employ various tools and techniques such as charting, trend
				analysis, and statistical models to make informed decisions. The
				asset market data is not only important for traders and investors
				but also for policymakers and regulators as they seek to ensure
				stability and efficiency in the financial system. The data is used
				to monitor the overall health of the market, identify potential
				risks and take necessary actions to mitigate them. In conclusion,
				the asset market data plays a vital role in the financial ecosystem
				and is critical for making informed decisions. With the advancements
				in technology, accessing and analyzing the data has become easier
				and more efficient, paving the way for new opportunities and
				innovations in the financial industry.</p>
		</div>
	</div>
</body>
</html>
