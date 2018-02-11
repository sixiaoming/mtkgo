#说明

##代码结构：

mobmktgo
    ├── src   
    │   
    ├── pom    
        
        
    
##相关接口定义：
全部概览接口

请求地址：http://{IP}:{PORT}/mktgo/overview_all

参数说明：
{
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201709”   月份
}

返回参数说明
{
"holding_rank": [{"key": "",
"value": 0
}
],
"hot_sell_price_rank": [{"key": "",
"value": 0
}
],
"hot_selling_rank": [{"key": "",
"value": 0
}
],
"market_share_brand": [
{
"data": [{"date": "",
"value": 0
}
],
"name": ""
}
],
"market_share_model": [
{
"data": [{"date": "",
"value": 0
}
],
"name": ""
}
],
"os_rank": [{"key": "",
"value": 0
}
],
"px_rank": [{"key": "",
"value": 0
}
]
}
品牌概览接口
请求地址：http://{IP}:{PORT}/mktgo/overview_brand

参数说明：
{
“brand”：“HUAWEI”，品牌
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201709”   月份
}

返回参数说明{"brand": "",
"brandPic": "",
"holding": 0,
"holding_rank": [{"key": "",
"value": 0
}
],
"hot_sell_price_rank": [{"key": "",
"value": 0
}
],
"hot_selling_rank": [{"key": "",
"value": 0
}
],
"market_share_brand": [
{
"data": [{"date": "",
"value": 0
}
],
"name": ""
}
],
"market_share_model": [
{
"data": [{"date": "",
"value": 0
}
],
"name": ""
}
],
"market_share_rank": 0,
"os_rank": [{"key": "",
"value": 0
}
],
"px_rank": [{"key": "",
"value": 0
}
]
}

地区分布接口
请求地址：http://{IP}:{PORT}/mktgo/regionDistribution
参数说明：
{
“brand”：“HUAWEI”，品牌
“model”：“HUAWEIP9‘”，
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201709”   月份
}
返回参数说明
{
"country": [{"key": "",
"value": 0
}
],
"countryMarketShare": [{"key": "",
"value": 0
}
],
"province": [{"key": "",
"value": 0
}
],
"provinceMarketShare": [{"key": "",
"value": 0
}
]
}

媒介分析接口
请求地址：http://{IP}:{PORT}/mktgo/mediaAnalysis
参数说明：
{
“brand”：“HUAWEI”，品牌
“model”：“HUAWEIP9‘”，
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201709”   月份
}
返回参数说明
[{"apppkg": "com.tencent.mm",
"icon": "http://img.wdjimg.com/mms/icon/v1/7/ed/15891412e00a12fdec0bbe290b42ced7_256_256.png",
"name": "微信",
"cate_id": "5014",
"cate_name": "通讯社交",
"cnt1": 12845519,
"cnt2": 15399505,
"cnt3": 179072076,
"cnt4": 221488764,
"active_rate": 0.834151422399616,
"index": 103.17363357988482
},{"apppkg": "com.tencent.mobileqq",
"icon": "http://img.wdjimg.com/mms/icon/v1/6/2a/4764bbc6717fe399db4eb2e5e41342a6_256_256.png",
"name": "QQ",
"cate_id": "5014",
"cate_name": "通讯社交",
"cnt1": 7153287,
"cnt2": 15399505,
"cnt3": 138073709,
"cnt4": 221488764,
"active_rate": 0.464514086654084,
"index": 74.51429505207395
}
]

换机周期接口
请求地址：http://{IP}:{PORT}/mktgo/changePhonePeriod
参数说明：
{
“brand”：“HUAWEI”，品牌
“model”：“HUAWEIP9‘”，机型
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
}
返回参数说明

{     "period": 
[{"key": "2017Q3","value": 10.4},{"key": "2017Q2","value": 9.8},{"key": "2017Q1","value": 8.7},{"key": "2016Q4","value": 7.5},{"key": "2016Q3","value": 6.2},{"key": "2016Q2","value": 5.7},{"key": "2016Q1","value": 3.9},{"key": "2015Q4","value": 3.3}]
 }

换机流动接口
请求地址：http://{IP}:{PORT}/mktgo/changePhoneTrend
参数说明：
{
“brand”：“HUAWEI”，品牌
“model”：“HUAWEIP9‘”，机型
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
}
返回参数说明
{
"source": {
"gender": [{"key": "男",
"value": 0.6560338743824983
},{"key": "女",
"value": 0.3439661256175018
}
],
"agebin": [{"key": "2534岁",
"value": 0.7081667758846658
},{"key": "18岁以下",
"value": 0.11161533420707732
},{"key": "1824岁",
"value": 0.10046690694626474
},{"key": "3544岁",
"value": 0.050630733944954126
},{"key": "45岁以上",
"value": 0.029120249017038007
}
],
"income": [{"key": ">10k",
"value": 0.490965884730814
},{"key": "4k10K",
"value": 0.31326432698393253
},{"key": "<4K",
"value": 0.19576978828525346
}
],
"segment": [{"key": "对战游戏爱好者",
"value": 0.2834917085895286
},{"key": "煲剧一族",
"value": 0.16277927401460102
},{"key": "语言学习者",
"value": 0.13867574571878652
},{"key": "购物达人",
"value": 0.1150126192049054
},{"key": "手机卫士达人",
"value": 0.07417382319562309
},{"key": "理财达人",
"value": 0.04180429222352084
},{"key": "出行达人",
"value": 0.03256432406795738
},{"key": "美拍达人",
"value": 0.025704219387841523
},{"key": "借贷一族",
"value": 0.022595999119196436
},{"key": "音乐爱好者",
"value": 0.017895557023561496
},{"key": "脱口秀爱好者",
"value": 0.01622711181123702
},{"key": "旅游达人",
"value": 0.010095363924318647
},{"key": "纸牌游戏爱好者",
"value": 0.008926605349187797
},{"key": "时尚达人",
"value": 0.006538272608703017
},{"key": "直播爱好者",
"value": 0.006165624947067094
},{"key": "空战游戏爱好者",
"value": 0.005615122719650389
},{"key": "早教一族",
"value": 0.005250944323051646
},{"key": "求职招聘一族",
"value": 0.004827481071192642
},{"key": "电子书爱好者",
"value": 0.004742788420820841
},{"key": "备孕一族",
"value": 0.0027863881972322443
},{"key": "养颜达人",
"value": 0.0019394616935142369
},{"key": "办公达人",
"value": 0.001854769043142436
},{"key": "策略塔防游戏爱好者",
"value": 0.0017616071277334552
},{"key": "买车一族",
"value": 0.0012873282856513712
},{"key": "休闲游戏爱好者",
"value": 0.0012280434303911106
},{"key": "健身达人",
"value": 0.0011264122499449497
},{"key": "电子产品爱好者",
"value": 0.0010417195995731491
},{"key": "买房一族",
"value": 0.0010163118044616089
},{"key": "保险达人",
"value": 0.0007452953232718465
},{"key": "换装游戏爱好者",
"value": 0.0006013178176397852
},{"key": "家政需求者",
"value": 0.0005166251672679845
},{"key": "听新闻一族",
"value": 0.0004912173721564443
},{"key": "电影迷",
"value": 0.0002879550112641225
},{"key": "体育爱好者",
"value": 0.00016938530074360147
},{"key": "动作游戏爱好者",
"value": 0.00005928485526026051
}
],
"occupation": [{"key": "消费品制造及贸易从业者",
"value": 0.1894305159676575
},{"key": "金融及财务从业者",
"value": 0.13802765012700444
},{"key": "服务业人员及个体户",
"value": 0.11379016151862409
},{"key": "地产从业者",
"value": 0.1081961491478099
},{"key": "信息通信从业者",
"value": 0.10173928218253737
},{"key": "政府及事业单位人员",
"value": 0.08179667655556203
},{"key": "能源材料从业者",
"value": 0.07959591258992109
},{"key": "学生",
"value": 0.058422042541640004
},{"key": "自由职业",
"value": 0.044247959203459176
},{"key": "交通物流从业者",
"value": 0.0230740891551781
},{"key": "媒体从业者",
"value": 0.02231787951059663
},{"key": "医疗从业者",
"value": 0.02135807496170477
},{"key": "教育培训从业者及专业人士",
"value": 0.018003606538304927
}
],
"edu": [{"key": "高中及以下",
"value": 0.7074428387271864
},{"key": "本科及专科",
"value": 0.27270700667810377
},{"key": "硕士及以上",
"value": 0.019850154594709856
}
],
"network": [key": "wifi",
"value": 0.8366510789823673
},{"key": "4g",
"value": 0.12250632802780043
},{"key": "cell",
"value": 0.015315972371187096
},{"key": "2g",
"value": 0.015251619546098074
},{"key": "3g",
"value": 0.010124844480672702
},{"key": "other",
"value": 0.00015015659187438328
}
],
"married": [ ],
"kids": [{"key": "无",
"value": 0.9703585551457149
},{"key": "有",
"value": 0.02964144485428505
}
],
"house": [ ],
"car": [key": "无车",
"value": 0.9631841792074032
},{"key": "有车",
"value": 0.03681582079259679
}
],
"carrier": [key": "CHINA UNICOM",
"value": 0.5247344303270152
},{"key": "CHINA MOBILE",
"value": 0.37335971672568213
},{"key": "CHINA TELECOM",
"value": 0.07618204540720683
},{"key": "VODAFONE LIBERTEL B.V.",
"value": 0.0010935221828785669
},{"key": "\"TMOBILE USA, INC.\"",
"value": 0.0010414496979795876
},{"key": "WIND TELECOMUNICAZIONI S.P.A.",
"value": 0.0009893772130806081
},{"key": "CHINA MOBILE HONG KONG COMPANY LIMITED",
"value": 0.0009893772130806081
},{"key": "ROGERS COMMUNICATIONS CANADA INC.",
"value": 0.0006769423036867319
},{"key": "MAXIS MOBILE SERVICES SDN BHD",
"value": 0.0006769423036867319
},{"key": "DTAC TRINET",
"value": 0.0006769423036867319
}
],
"sourceModel": [{"key": "华为P9",
"value": 0.05513684290662708
},{"key": "华为P8",
"value": 0.026302767309961554
},{"key": "华为MATE 7",
"value": 0.025655665943435726
},{"key": "荣耀7",
"value": 0.01720528339233375
},{"key": "OPPO R9",
"value": 0.01688173270907084
},{"key": "华为ASCEND P7",
"value": 0.015435270830954284
},{"key": "OPPO R7",
"value": 0.014179132884168855
},{"key": "荣耀畅玩4X",
"value": 0.01332267519317879
},{"key": "三星GALAXY S5",
"value": 0.012066537246393361
},{"key": "华为P8青春版",
"value": 0.011952342887594686
}
]
},
"trend": {

"gender": [key": "男",
"value": 0.6502375478927203
},{"key": "女",
"value": 0.3497624521072797
}
],
"agebin": [{"key": "2534岁",
"value": 0.4135573239021515
},{"key": "1824岁",
"value": 0.24255820807544945
},{"key": "18岁以下",
"value": 0.1431476569407604
},{"key": "3544岁",
"value": 0.11842027704096669
},{"key": "45岁以上",
"value": 0.08231653404067198
}
],
"income": [{"key": ">10k",
"value": 0.3979949874686717
},{"key": "<4K",
"value": 0.37334512752469406
},{"key": "4k10K",
"value": 0.22865988500663423
}
],
"segment": [{"key": "购物达人",
"value": 0.2543224471700901
},{"key": "对战游戏爱好者",
"value": 0.19199054233781587
},{"key": "煲剧一族",
"value": 0.08636027781882666
},{"key": "理财达人",
"value": 0.08476429732525491
},{"key": "脱口秀爱好者",
"value": 0.06247968080390129
},{"key": "手机卫士达人",
"value": 0.05718930101965421
},{"key": "语言学习者",
"value": 0.05547509974878085
},{"key": "旅游达人",
"value": 0.029968967045958327
},{"key": "办公达人",
"value": 0.02396926259790158
},{"key": "出行达人",
"value": 0.021723067829170977
},{"key": "时尚达人",
"value": 0.021043298359686716
},{"key": "空战游戏爱好者",
"value": 0.014629821191074331
},{"key": "直播爱好者",
"value": 0.012767843948573962
},{"key": "借贷一族",
"value": 0.012501847199645338
},{"key": "美拍达人",
"value": 0.012265405645042117
},{"key": "音乐爱好者",
"value": 0.009989655681986108
},{"key": "求职招聘一族",
"value": 0.00889611349194621
},{"key": "电子书爱好者",
"value": 0.008837003103295405
},{"key": "策略塔防游戏爱好者",
"value": 0.006649918723215605
},{"key": "纸牌游戏爱好者",
"value": 0.0038421752623023497
},{"key": "养颜达人",
"value": 0.003044185015516477
},{"key": "早教一族",
"value": 0.002778188266587853
},{"key": "听新闻一族",
"value": 0.002453081129008423
},{"key": "买车一族",
"value": 0.002275749963056007
},{"key": "健身达人",
"value": 0.0019506428254765775
},{"key": "换装游戏爱好者",
"value": 0.0019210876311511749
},{"key": "电子产品爱好者",
"value": 0.0015664252992463425
},{"key": "保险达人",
"value": 0.0012413181616669129
},{"key": "休闲游戏爱好者",
"value": 0.0010935421900398995
},{"key": "备孕一族",
"value": 0.0009162110240874834
},{"key": "电影迷",
"value": 0.00038421752623023496
},{"key": "买房一族",
"value": 0.0002955519432540269
},{"key": "家政需求者",
"value": 0.0002659967489286242
},{"key": "动作游戏爱好者",
"value": 0.00008866558297620807
},{"key": "体育爱好者",
"value": 0.00005911038865080538
}
],
"occupation": [{"key": "信息通信从业者",
"value": 0.18641320365458297
},{"key": "学生",
"value": 0.17727674624226347
},{"key": "消费品制造及贸易从业者",
"value": 0.1251694665487769
},{"key": "自由职业",
"value": 0.10583554376657825
},{"key": "服务业人员及个体户",
"value": 0.07839669908635426
},{"key": "金融及财务从业者",
"value": 0.06472148541114059
},{"key": "交通物流从业者",
"value": 0.05585027998821102
},{"key": "医疗从业者",
"value": 0.040318302387267906
},{"key": "地产从业者",
"value": 0.040023577954612435
},{"key": "教育培训从业者及专业人士",
"value": 0.039610963748894785
},{"key": "政府及事业单位人员",
"value": 0.0394930739758326
},{"key": "能源材料从业者",
"value": 0.03557323902151489
},{"key": "媒体从业者",
"value": 0.011317418213969938
}
],
"edu": [{"key": "高中及以下",
"value": 0.48299440023577955
},{"key": "本科及专科",
"value": 0.48267020335985855
},{"key": "硕士及以上",
"value": 0.03433539640436192
}
],
"network": [{"key": "wifi",
"value": 0.6969685356225808
},{"key": "4g",
"value": 0.255202544743801
},{"key": "cell",
"value": 0.028436745487295443
},{"key": "3g",
"value": 0.015329781933851991
},{"key": "2g",
"value": 0.0037174721189591076
},{"key": "other",
"value": 0.0003449200935116698
}
],
"married": [ ],
"kids": [{"key": "无",
"value": 0.9062066932035973
},{"key": "有",
"value": 0.09379330679640277
}
],
"house": [ ],
"car": [{"key": "无车",
"value": 0.8921603300913645
},{"key": "有车",
"value": 0.10783966990863543
}
],
"carrier": [{"key": "CHINA MOBILE",
"value": 0.5583975840215973
},{"key": "CHINA UNICOM",
"value": 0.22757326866320438
},{"key": "CHINA TELECOM",
"value": 0.20464892813836968
},{"key": "VODAFONE AUSTRALIA PTY LTD",
"value": 0.0004575716671623693
},{"key": "MAXIS MOBILE SERVICES SDN BHD",
"value": 0.0004118145004461324
},{"key": "VODAFONE LIBERTEL B.V.",
"value": 0.0004118145004461324
},{"key": "CHINA MOBILE HONG KONG COMPANY LIMITED",
"value": 0.000343178750371777
},{"key": "\"TMOBILE USA, INC.\"",
"value": 0.00029742158365554005
},{"key": "DTAC TRINET",
"value": 0.0002516644169393031
},{"key": "DIGI TELECOMMUNICATIONS SDN BHD",
"value": 0.0002516644169393031
}
],
"trendModel": [key": "华为P10",
"value": 0.08011092623405436
},{"key": "华为P9",
"value": 0.06427066001109262
},{"key": "华为MATE 9",
"value": 0.052712146422628954
},{"key": "华为MATE 9保时捷版",
"value": 0.024669994453688296
},{"key": "OPPO R11",
"value": 0.021475318912922908
},{"key": "小米6",
"value": 0.0207210205213533
},{"key": "VIVO X9",
"value": 0.013044925124792014
},{"key": "小米NOTE",
"value": 0.011447587354409318
},{"key": "OPPO R9S",
"value": 0.011336661120354963
},{"key": "三星GALAXY S8",
"value": 0.010848585690515807
}
]
},
"modelInfo": {"brand": "HUAWEI",
"model": "华为P9",
"picAddress": ["http://2a.zolimg.com.cn/product/172_120x90/906/cew82ndPs5Qc.jpg",
"http://2f.zolimg.com.cn/product/172_120x90/899/cexCpNGNRWCfw.jpg",
"http://2a.zolimg.com.cn/product/175_120x90/170/cez4KlnefCag.jpg",
"http://2a.zolimg.com.cn/product/172_120x90/900/ceUPPjaRGUW4c.jpg",
"http://2d.zolimg.com.cn/product/172_120x90/167/ceEaRpesQmqME.jpg",
"http://2c.zolimg.com.cn/product/172_120x90/172/ceVflzTGfFudc.jpg",
"http://2f.zolimg.com.cn/product/172_120x90/169/ce2ve2e90kmR6.jpg",
"http://2b.zolimg.com.cn/product/172_120x90/165/cea9v0WxPMQSk.jpg",
"http://2d.zolimg.com.cn/product/172_120x90/173/ceRJqzeXWZ4Y.jpg",
"http://2a.zolimg.com.cn/product/172_120x90/182/ceKhXMbXaaqs2.jpg",
"http://2b.zolimg.com.cn/product/172_120x90/907/ceadyyKsC37zU.jpg",
"http://2c.zolimg.com.cn/product/172_120x90/908/cecScaSIcUmqg.jpg",
"http://2e.zolimg.com.cn/product/172_120x90/898/ceUXeiK28pRS6.jpg",
"http://2c.zolimg.com.cn/product/172_120x90/154/ceu2gyVgsRwnY.jpg",
"http://2e.zolimg.com.cn/product/172_120x90/910/ces5OdEfpXcFs.jpg",
"http://2b.zolimg.com.cn/product/172_120x90/475/cepj3DHY3hP5c.jpg"
],
"price": "2090",
"publicTime": "2016年04月"
}
}


机型概览接口
请求地址：http://{IP}:{PORT}/mktgo/overview_model
参数说明：
{
“model”：“HUAWEIP9‘”，机型
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201802”
}
返回参数说明
{
"ageBins": [{"key": "",
"value": 0
}
],
"genders": [{"key": "",
"value": 0
}
],
"market_share_model": [
{
"data": [{"date": "",
"value": 0
}
],
"name": ""
}
],
"model": "",
"pics": [ ],
"price": "",
"uptime": ""
}

标准画像接口
请求地址：http://{IP}:{PORT}/mktgo/standardPortrait
参数说明：
{
“brand”：“HUAWEI”，品牌
“model”：“HUAWEIP9‘”，机型
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19”， 省份
“date”：“201802”
}
返回参数说明
{
"gender": [key": "男",
"value": 0.6637001247995594key": "女",
"value": 0.3362998752004405
}
],
"agebin": [key": "2534岁",
"value": 0.4740316741683013key": "1824岁",
"value": 0.17087138630396714key": "3544岁",
"value": 0.1454950285941448key": "45岁以上",
"value": 0.12373277243001236key": "18岁以下",
"value": 0.08586913850357447
}
],
"income": [key": "<4K",
"value": 0.43995075958838775key": "4k10K",
"value": 0.29018757181607385key": ">10k",
"value": 0.26986166859553845
}
],
"segment": [key": "购物达人",
"value": 0.18067397990624579key": "手机卫士达人",
"value": 0.14390913668808883key": "对战游戏爱好者",
"value": 0.13879244630556864key": "理财达人",
"value": 0.12114050156934389key": "煲剧一族",
"value": 0.0866556385158459key": "出行达人",
"value": 0.04463835557390041key": "空战游戏爱好者",
"value": 0.04214297040936385key": "脱口秀爱好者",
"value": 0.037060945457554915key": "语言学习者",
"value": 0.03589932993077501key": "音乐爱好者",
"value": 0.02233393183111815key": "旅游达人",
"value": 0.01839502676059003key": "美拍达人",
"value": 0.014127942803576416key": "直播爱好者",
"value": 0.0136568650835493key": "纸牌游戏爱好者",
"value": 0.01277122804846741key": "借贷一族",
"value": 0.012608498727870716key": "策略塔防游戏爱好者",
"value": 0.01130319559753884key": "求职招聘一族",
"value": 0.008865360306449688key": "早教一族",
"value": 0.007908289023251028key": "电子产品爱好者",
"value": 0.007601932773718539key": "买车一族",
"value": 0.006199368233648967key": "时尚达人",
"value": 0.005722283726619959key": "健身达人",
"value": 0.0052121618910805495key": "办公达人",
"value": 0.004216875712124066key": "电子书爱好者",
"value": 0.0035742202899739007key": "养颜达人",
"value": 0.003081542406586448key": "换装游戏爱好者",
"value": 0.0028753498359322983key": "备孕一族",
"value": 0.0023663909305363523key": "听新闻一族",
"value": 0.0018736422600966883key": "保险达人",
"value": 0.0018658455719174657key": "休闲游戏爱好者",
"value": 0.0014213534462135353key": "家政需求者",
"value": 0.0004018176456568235key": "买房一族",
"value": 0.00038693213982048667key": "电影迷",
"value": 0.00017440918421148194key": "动作游戏爱好者",
"value": 0.00009066810144605711key": "体育爱好者",
"value": 0.00005156331131758256
}
],
"occupation": [key": "信息通信从业者",
"value": 0.19486603841094022key": "消费品制造及贸易从业者",
"value": 0.15362710717307954key": "学生",
"value": 0.13181307634076594key": "自由职业",
"value": 0.11578997010932243key": "服务业人员及个体户",
"value": 0.06534871876183607key": "金融及财务从业者",
"value": 0.06009506590736934key": "交通物流从业者",
"value": 0.05159295813735478key": "医疗从业者",
"value": 0.04906995306750754key": "政府及事业单位人员",
"value": 0.043469050263984316key": "能源材料从业者",
"value": 0.04095815516711894key": "教育培训从业者及专业人士",
"value": 0.0408534975969562key": "地产从业者",
"value": 0.0387737658975383key": "媒体从业者",
"value": 0.013742643166226386
}
],
"edu": [key": "高中及以下",
"value": 0.5353027071109137key": "本科及专科",
"value": 0.4386760035565842key": "硕士及以上",
"value": 0.02602128933250212
}
],
"network": [{"key": "wifi",
"value": 0.7493471818775898{"key": "4g",
"value": 0.18141308271305132{"key": "3g",
"value": 0.029114474628900905key": "cell",
"value": 0.02778569511521453key": "2g",
"value": 0.01198371793633967key": "other",
"value": 0.00035584772890381416
}
],
"married": [ ],
"kids": [key": "无",
"value": 0.9055640011947211key": "有",
"value": 0.0944359988052789
}
],
"house": [ ],
"car": [key": "无车",
"value": 0.9090438870055295key": "有车",
"value": 0.0909561129944705
}
],
"carrier": [key": "CHINA MOBILE",
"value": 0.5468150436544219key": "CHINA TELECOM",
"value": 0.20590564236197686key": "CHINA UNICOM",
"value": 0.18742194844389246key": "MYANMA POSTS AND TELECOMMUNICATIONS",
"value": 0.004127665892839783key": "TELENOR MYANMAR",
"value": 0.003748761217570867key": "MTN IRANCELL",
"value": 0.0023520150948334218key": "MCI",
"value": 0.002184520509114144key": "OOREDOO",
"value": 0.0020020611994397072key": "ADVANCED WIRELESS NETWORK",
"value": 0.0017330876354608904key": "SAFARICOM",
"value": 0.0015490802508945404
}
]
}

粉丝画像接口

请求地址：http://{IP}:{PORT}/mktgo/fansPortrait

参数说明：
{
“brand”:“HUAWEI”，
“model”:”HUAWEIP9”，
“price”：“10001999”， 价格
“country”：“cn”， 国家
“province”：“cn19” 省份
}

返回参数说明
{
"gender": [
{"key": "男",
"value": 0.6522181177347748
},
{"key": "女",
"value": 0.3477818822652252
}
],
"agebin": [
{"key": "25-34岁",
"value": 0.351738492731116
},
{"key": "18-24岁",
"value": 0.2670721711746689
},
{"key": "18岁以下",
"value": 0.1654695655925111
},
{"key": "35-44岁",
"value": 0.11764807385358698
},
{"key": "45岁以上",
"value": 0.09807169664811699
}
],
"income": [
{"key": "<4K",
"value": 0.3679501148954074
},
{"key": ">10k",
"value": 0.35091970267441985
},
{"key": "4k-10K",
"value": 0.28113018243017274
}
],
"segment": [
{"key": "对战游戏爱好者",
"value": 0.2424236525229978
},
{"key": "购物达人",
"value": 0.20138559937014278
},
{"key": "煲剧一族",
"value": 0.10022559789932234
},
{"key": "理财达人",
"value": 0.08692981374654736
},
{"key": "手机卫士达人",
"value": 0.08296725045260173
},
{"key": "语言学习者",
"value": 0.06555100610606779
},
{"key": "脱口秀爱好者",
"value": 0.03940933581642431
},
{"key": "出行达人",
"value": 0.02140908923770405
},
{"key": "旅游达人",
"value": 0.019378059252433882
},
{"key": "空战游戏爱好者",
"value": 0.019233140180002467
},
{"key": "直播爱好者",
"value": 0.016445070264120416
},
{"key": "电子书爱好者",
"value": 0.015478222124167527
},
{"key": "美拍达人",
"value": 0.012770182143807306
},
{"key": "借贷一族",
"value": 0.011615155506816605
},
{"key": "求职招聘一族",
"value": 0.011355598959178244
},
{"key": "策略塔防游戏爱好者",
"value": 0.009082316196112276
},
{"key": "音乐爱好者",
"value": 0.006640321677081373
},
{"key": "纸牌游戏爱好者",
"value": 0.006305061136381825
},
{"key": "办公达人",
"value": 0.004819099901152215
},
{"key": "时尚达人",
"value": 0.0045141209576771415
},
{"key": "养颜达人",
"value": 0.003581880357409366
},
{"key": "早教一族",
"value": 0.0030627672621326464
},
{"key": "买车一族",
"value": 0.0024225277779580255
},
{"key": "电子产品爱好者",
"value": 0.0022365122521505344
},
{"key": "换装游戏爱好者",
"value": 0.0019423481648270595
},
{"key": "休闲游戏爱好者",
"value": 0.0017822882937834044
},
{"key": "健身达人",
"value": 0.0017693104664014863
},
{"key": "听新闻一族",
"value": 0.0015443614584482411
},
{"key": "备孕一族",
"value": 0.0013886275298652252
},
{"key": "保险达人",
"value": 0.0012437084574338077
},
{"key": "电影迷",
"value": 0.00035256431054210547
},
{"key": "买房一族",
"value": 0.0003136308283963515
},
{"key": "家政需求者",
"value": 0.00027902328871123687
},
{"key": "动作游戏爱好者",
"value": 0.00009517073413406528
},
{"key": "体育爱好者",
"value": 0.00004758536706703264
}
],
"occupation": [
{"key": "学生",
"value": 0.2083300979250248
},
{"key": "信息通信从业者",
"value": 0.19138734308269703
},
{"key": "自由职业",
"value": 0.13607695957896554
},
{"key": "消费品制造及贸易从业者",
"value": 0.11707217117466891
},
{"key": "服务业人员及个体户",
"value": 0.05972563737543678
},
{"key": "金融及财务从业者",
"value": 0.0548487985850481
},
{"key": "交通物流从业者",
"value": 0.04265993701738493
},
{"key": "医疗从业者",
"value": 0.03941805789223934
},
{"key": "地产从业者",
"value": 0.038136836202062034
},
{"key": "教育培训从业者及专业人士",
"value": 0.03799447823648678
},
{"key": "能源材料从业者",
"value": 0.03235624002415771
},
{"key": "政府及事业单位人员",
"value": 0.03164229325740908
},
{"key": "媒体从业者",
"value": 0.010351149648418963
}
],
"edu": [
{"key": "高中及以下",
"value": 0.5518981062076701
},
{"key": "本科及专科",
"value": 0.42746214572279023
},
{"key": "硕士及以上",
"value": 0.02063974806953971
}
],
"network_new": [
{"key": "wifi",
"value": 0.7302767570880517
},
{"key": "4g",
"value": 0.2312380203469799
},
{"key": "cell",
"value": 0.02541782427818525
},
{"key": "3g",
"value": 0.010086759429775743
},
{"key": "2g",
"value": 0.002899690372045019
},
{"key": "other",
"value": 0.00008094848496237341
}
],
"married": [ ],
"kids": [
{"key": "无",
"value": 0.9223717001283808
},
{"key": "有",
"value": 0.07762829987161922
}
],
"house": [ ],
"car": [
{"key": "无车",
"value": 0.9151115137397006
},
{"key": "有车",
"value": 0.08488848626029938
}
],
"carrier_new": [
{"key": "CHINA MOBILE",
"value": 0.5949507604895771
},
{"key": "CHINA UNICOM",
"value": 0.25441365758547413
},
{"key": "CHINA TELECOM",
"value": 0.1434857612892907
},
{"key": "\"T-MOBILE USA, INC.\"",
"value": 0.0007330708241620144
},
{"key": "CHINA MOBILE HONG KONG COMPANY LIMITED",
"value": 0.0002779956891627379
},
{"key": "MYANMA POSTS AND TELECOMMUNICATIONS",
"value": 0.00026466712872342857
},
{"key": "TELENOR MYANMAR",
"value": 0.00024943448822136073
},
{"key": "OOREDOO",
"value": 0.0002437222480330853
},
{"key": "VODAFONE LIBERTEL B.V.",
"value": 0.00021135288696619116
},
{"key": "VODAFONE AUSTRALIA PTY LTD",
"value": 0.00020183248665239876
}
]
}


