sap.ui.jsview("marketinglead.LeadDetails", {

	/** Specifies the Controller belonging to this View. 
	* In the case that it is not implemented, or that "null" is returned, this View does not have a Controller.
	* @memberOf marketinglead.LeadDetails
	*/ 
	getControllerName : function() {
		return "marketinglead.LeadDetails";
	},

	/** Is initially called once after the Controller has been instantiated. It is the place where the UI is constructed. 
	* Since the Controller is given to this method, its event handlers can be attached right away. 
	* @memberOf marketinglead.LeadDetails
	*/ 
	createContent : function(oController) {
	

		 		//Create an instance of the table control
		 		var oTable = new sap.ui.table.Table({
		 			title: "Marketing Lead",
		 			visibleRowCount: 10,
		 			firstVisibleRow: 3,
		 			selectionMode: sap.ui.table.SelectionMode.Single,
		 			navigationMode: sap.ui.table.NavigationMode.Paginator,
		 			fixedColumnCount: 2
		 		});
		 		var oTableToolbar = new sap.ui.commons.Toolbar();
		 		//Define the columns and the control templates to be used
		 		oTable.addColumn(new sap.ui.table.Column({
		 			label: new sap.ui.commons.Label({text: "Marketing Lead ID"}),
		 			template: new sap.ui.commons.TextView().bindProperty("text", "leadid"),
		 			sortProperty: "leadid",
		 			//filterProperty: "leadid",
		 			width: "200px"
		 		}));		 		

		 		oTable.addColumn(new sap.ui.table.Column({
		 			label: new sap.ui.commons.Label({text: "First Name"}),
		 			template: new sap.ui.commons.TextField().bindProperty("value", "firstName"),
		 			sortProperty: "firstName",
		 			//filterProperty: "firstName",
		 			width: "200px"
		 		}));
		 		oTable.addColumn(new sap.ui.table.Column({
		 			label: new sap.ui.commons.Label({text: "Last Name"}),
		 			template: new sap.ui.commons.TextView().bindProperty("text", "lastName"),
		 			sortProperty: "lastName",
		 			//filterProperty: "lastName",
		 			width: "200px"
		 		}));
		 		

		 		var oUpdateModel = new sap.ui.commons.Button({
             		id : 'UpdateModelId',
             		text : "Refresh",
             		icon : "images/refresh-icon.gif",
             		press : function() {
             			oController.updatePromotionData(oTable);
             		}
             		});
             		oTableToolbar.addItem(oUpdateModel);
             		oTable.setToolbar(oTableToolbar);
             	
             	oController.getPromotionData(oTable);

		 		//Initially sort the table
		 		//oTable.sort(oTable.getColumns()[0]);
		 		
		 		return oTable;

		 	}

	
	

});
