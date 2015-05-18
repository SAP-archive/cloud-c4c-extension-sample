sap.ui.controller("marketinglead.LeadDetails", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf marketinglead.LeadDetails
*/
	//onInit: function() {
	//	
	//},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf marketinglead.LeadDetails
*/
//	onBeforeRendering: function() {
//
//	},
	getPromotionData: function(oTable)
	{ 
		var relativePromotionOdataServiceUrl = "/MarketingLead/odata/jpa.svc";
		var sOrigin = window.location.protocol + "//"
		+ window.location.hostname
		+ (window.location.port ? ":" + window.location.port : "");
		var odataModel = new sap.ui.model.odata.ODataModel(sOrigin +relativePromotionOdataServiceUrl);		
		
		oTable.setModel(odataModel);		
		oTable.bindRows("/MarketingLead");
	},
	
	updatePromotionData: function(oTable)
	{
		var relativePromotionOdataServiceUrl = "/MarketingLead/odata/jpa.svc";
		var sOrigin = window.location.protocol + "//"
		+ window.location.hostname
		+ (window.location.port ? ":" + window.location.port : "");
		var odataModel = new sap.ui.model.odata.ODataModel(sOrigin +relativePromotionOdataServiceUrl);
		// Sort by ProductID
		var oSorter = new sap.ui.model.Sorter("leadid",true);
		oTable.setModel(odataModel);
		oTable.unbindRows().bindRows("/MarketingLead",oSorter);
		
	},
/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf marketinglead.LeadDetails
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf marketinglead.LeadDetails
*/
//	onExit: function() {
//
//	}

});