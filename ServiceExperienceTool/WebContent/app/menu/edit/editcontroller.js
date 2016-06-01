'use strict';

angular.module('menu')

.controller('MenuEditController', ['$scope', 'MenuService', 'Category', 'Allergen', function($scope, MenuService, Category, Allergen) {
	
//    $scope.menuList = [];
//    $scope.categoryList = [];
//    $scope.allergenList = [];
    
    $scope.selectedView = 0;
    $scope.selectedMenu = 0;
	$scope.selectedCategory = -1;
	$scope.selectedSubCategory = -1;
	
	$scope.itemForm = { visible: false, data: {} };
	$scope.categoryForm = { visible: false, data: {} };
	$scope.allergenForm = { visible: false, data: {} };
	
    
    
    $scope.changeView = function(viewNumber){
    	$scope.selectedView = viewNumber;
    };
    
    $scope.toggleModal = function(modal){
    	modal.visible = !modal.visible;
    };

    $scope.prepopulateAndToggle = function(data, modal){
    	switch(true){
    	case (modal === $scope.itemForm):
    		$scope.prepopulateItem(data);
    		break;
    	case (modal === $scope.categoryForm):
    		$scope.prepopulateCategory(data);
    		break;
    	case (modal === $scope.allergenForm):
    		$scope.prepopulateAllergen(data);
    	break;
    	} 
    	$scope.toggleModal(modal);
    };
    
//Form Pre-population logic
    $scope.prepopulateItem = function(item){
    	$scope.itemForm.data = {};
    	angular.forEach(item, function(value, key){
    		$scope.itemForm.data[key] = angular.copy(value);
    	});
    	if(!$scope.itemForm.data.allergens){
    		$scope.itemForm.data.allergens = [];
    	}
    };
    $scope.prepopulateCategory = function(category){
    	$scope.categoryForm.data = {};
    	angular.forEach(category, function(value, key){
    		$scope.categoryForm.data[key] = angular.copy(value);
    	}); 
    }
    $scope.prepopulateAllergen = function(allergen){
    	$scope.allergenForm.data = {};
    	angular.forEach(allergen, function(value, key){
    		$scope.allergenForm.data[key] = angular.copy(value);
    		console.log('key: '+key+', value:' +value);
    	}); 
    	console.log($scope.allergenForm.data);
    }
    
    
//Category form selection options filtering  
    $scope.getValidCategories = function(category){
    	var subList = [];
    	if(category.parent > 0){
    		if(!category.id){
    			angular.forEach($scope.categoryList, function(cat){
    				if(cat.id == category.parent) subList.push(cat);
    				return;
    			});
    		}else{
    			angular.forEach($scope.categoryList, function(cat){
    				if(cat.parent == 0) subList.push(cat);
    			});
    		}
    	}
    	return subList;
    };
    
//Item form checkbox updating logic
    $scope.isChecked = function(id, source){
    	var match = false;
    	angular.forEach(source, function(item){
    		if (item.id == id){
    			match = true;
    			return;
    		}
    	});
    	this.flag = match;
    	return match;
    };    
    $scope.syncCheckboxSelection = function(selected, item, target){
    	if(selected){
    		target.push(item);
    	} else {
    		for(var i=0; i < target.length; i++) {
    			if(target[i].id == item.id){
    				target.splice(i,1);
    				break;
    			}
    		}
    	}
    };
    
    
    
    $scope.submitItemForm = function(){
    	if($scope.itemForm.data.id){
    		for(var i=0; i < $scope.menuItemList1.length; i++){
    			if($scope.menuItemList1[i].id == $scope.itemForm.data.id){
    				$scope.menuItemList1[i] = $scope.itemForm.data;
    				break;
    			}
    		}
    	} else {
        	var highest = 0;
        	angular.forEach($scope.menuItemList1, function(item){
        		if(highest < item.id) highest = item.id;
        	});
        	$scope.itemForm.data.id = highest+1;
        	$scope.menuItemList1.push($scope.itemForm.data);
    	}
    	$scope.toggleModal($scope.itemForm);
    };
    
    $scope.submitCategoryForm = function(){
    	if($scope.categoryForm.data.id){
    		for(var i=0; i < $scope.categoryList.length; i++){
    			if($scope.categoryList[i].id == $scope.categoryForm.data.id){
    				$scope.categoryList[i] = $scope.categoryForm.data;
    				break;
    			}
    		}
    	} else {
        	var highest = 0;
        	angular.forEach($scope.categoryList, function(cat){
        		if(highest < cat.id) highest = cat.id;
        	});
        	$scope.categoryForm.data.id = highest+1;
        	$scope.categoryList.push($scope.categoryForm.data);
    	}
    	$scope.toggleModal($scope.categoryForm);
    };
    
    $scope.submitAllergenForm = function(){
    	if($scope.allergenForm.data.id){
    		for(var i=0; i < $scope.allergenList.length; i++){
    			if($scope.allergenList[i].id == $scope.allergenForm.data.id){
    				$scope.allergenList[i] = $scope.allergenForm.data;
    				break;
    			}
    		}
    	} else {
        	var highest = 0;
        	angular.forEach($scope.allergenList, function(allergen){
        		if(highest < allergen.id) highest = allergen.id;
        	});
        	$scope.allergenForm.data.id = highest+1;
        	$scope.allergenList.push($scope.allergenForm.data);
    	}
    	$scope.toggleModal($scope.allergenForm);
    };
    
    $scope.deleteAllergen = function(allergen){
    	angular.forEach($scope.itemList1, function(item){
    		for(var i=0; i < item.allergens.length; i++){
    			if(item.allergens[i].id == allergen.id){
    				item.allergens.splice(i,1);
    			}
    		}
    	});
    	var index = $scope.allergenList.indexOf(allergen);
    	$scope.allergenList.splice(index, 1);
    };
    $scope.deleteCategory = function(category){
    	if(category.parent == 0){
    		var empty = true;
    		angular.forEach($scope.categoryList, function(cat){
    			if(cat.parent == category.id){
    				empty = false;
    				return;
    			}
    		});
    		if(!empty) return;
    	}
    	angular.forEach($scope.itemList1, function(item){
    		if(item.category == category.id){
    			item.category = null;
    		}
    	});

    	var index = $scope.categoryList.indexOf(category);
    	$scope.categoryList.splice(index, 1);
    };
    $scope.deleteMenuItem = function(item){
    	var index = $scope.menuItemList1.indexOf(item);
    	$scope.menuItemList1.splice(index, 1);
    }
    
/*
 * Inline test data
 */
    $scope.menuList = [
                       {"id": 1,"name": "Menu 1"},
//                       {"id": 2,"name": "Menu 2"}
                       ];
    
    $scope.allergenList = [
                           {id: '1', name: 'gluten'},
                           {id: '2', name: 'laktos'},
                           {id: '3', name: 'nötter'},
                           {id: '4', name: 'ägg'}
                           ];
     
    $scope.categoryList = [
                           {
                        	    "id": 1,
                        	    "name": "2-rättersmiddagar",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 2,
                        	    "name": "3-rättersmiddagar",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 3,
                        	    "name": "Desserter",
                        	    "parent": 12
                        	  },
                        	  {
                        	    "id": 4,
                        	    "name": "Frukost",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 5,
                        	    "name": "Lunchbufféer",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 6,
                        	    "name": "Övriga Varmrätter",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 7,
                        	    "name": "Baguetter \u0026 Smörgåsar",
                        	    "parent": 11
                        	  },
                        	  {
                        	    "id": 8,
                        	    "name": "Fika \u0026 kakor",
                        	    "parent": 12
                        	  },
                        	  {
                        	    "id": 9,
                        	    "name": "Dryck",
                        	    "parent": 0
                        	  },
                        	  {
                        	    "id": 10,
                        	    "name": "Välkomstbål \u0026 snacks",
                        	    "parent": 12
                        	  },
                        	  {
                        	    "id": 11,
                        	    "name": "Mat",
                        	    "parent": 0
                        	  },
                        	  {
                        	    "id": 12,
                        	    "name": "Övrigt",
                        	    "parent": 0
                        	  }
                        	];
    
    
    $scope.menuItemList1 = [
  {
    "id": 1,
    "name": "Vildmarksbuffé",
    "description": "Stor vildmarksbuffé där ni själva kombinerar och tillagar era favoriträtter. Välj mellan ett stort utbud av oxkött, fläskkött, kyckling, revben, bacon, grönsaker och frukter som ni grillar eller wokar. Alla läckerheter avnjutes med bakad potatis, potatissallad, ris, kryddsmör, sallad, olika dressingar, bröd, smör och dryck.\nTill dessert serverar vi Forest Delicious: Varma skogsbär med glass, grädde, chokladsås och rån. Kaffe och te serveras till.",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 2,
    "name": "Hunter\u0027s Wild Meal Buffet",
    "description": "Huvudrätt: Kombinera och grilla själv dina läckra grillspett med fläskkött, färska champinjoner, majs, paprika, äpple, lök, köttbullar, korv och bacon. Serveras med bakad potatis, kryddsmör, barbequesås, sallad, dressing, bröd, smör och dryck.\nDessert: Blueberry pie with custard.",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 3,
    "name": "Digger\u0027s Spareribs Buffet",
    "description": "Huvudrätt: Saftiga nygrillade revbensspjäll med en god barbequesås, bakad potatis med kryddsmör, sallad, dressing, bröd, smör och läsk/lättöl/vatten.\nDessert: Ugnsstekta toscaäpplen med vaniljglass och kaffe",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 4,
    "name": "Western Dinner",
    "description": "Huvudrätt: Goda revben, buffalo wings, majskolvar, Texasbönor, countrypotatis, coleslaw och barbequesås.\nDessert: Äppelpaj med vaniljsås.",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 5,
    "name": "Cowboy Meal",
    "description": "Huvudrätt: Välkryddade karrékotletter med stekt potatis, paprika, lök, svamp och stekta tomater serverade med varma majskolvar.\nDessert: Chocolate chip cookies.",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 6,
    "name": "Wok Duell",
    "description": "Huvudrätt: Utmana varandra i att laga den godaste woken och servera den på det mest kreativa sättet. Grunden i de olika wokarna är oxkött, fläskkött, fågel och skaldjur. Ni hackar, skär och wokar er favoriträtt med ingredienser som färska grönsaker, strimlade rotfrukter, bambuskott och spännande kryddor. Serveras med jasminris, olika såser, sallad, dressing, smör, och bröd. Inkl läsk/lättöl/mineralvatten.\nDessert: Varma mintpäron med glass och kaffe.",
    "details": "Inga detaljer tillgängliga",
    "category": 1,
    "allergens": []
  },
  {
    "id": 7,
    "name": "Miner\u0027s Explosion Meal",
    "description": "Förrätt: Räkcocktail med kuvertbröd\nMain course: Ugnsstekt örtfylld fläskfilé, potatisgratäng, rödvinssås, herrgårdsgrönsaker och svart vinbärsgelé.\nDessert: Varma mintpäron med glass",
    "details": "Inga detaljer tillgängliga",
    "category": 2,
    "allergens": []
  },
  {
    "id": 8,
    "name": "Vildsvinsbuffé",
    "description": "Förrätt: Skogssvampskrustad\nHuvudrätt: Enbärskryddad vildsvinsstek, kokt potatis, murkelsås, brysselkål och rönnbärsgelé.\nDessert: Vaniljglass med varm hjortronsylt och knäckflarn.",
    "details": "Inga detaljer tillgängliga",
    "category": 2,
    "allergens": []
  },
  {
    "id": 9,
    "name": "Äppelpaj",
    "description": "med vaniljsås",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 10,
    "name": "Blåbärspaj",
    "description": "med vaniljsås",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 11,
    "name": "Stekta toscaäpplen",
    "description": "med glass",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 12,
    "name": "Varma mintpäron",
    "description": "med glass",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 13,
    "name": "Forest Deliciuos",
    "description": "Vaniljglass med varma skogsbär, vispad grädde och chokladsås",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 14,
    "name": "Grillad banan",
    "description": "med choklad och glass",
    "details": "",
    "category": 3,
    "allergens": []
  },
  {
    "id": 15,
    "name": "Skogshuggarfrukost",
    "description": "Rejäl frukost för den hungrige!\nKaffe, te, chokladmjölk, juice, fil, yoghurt, mjölk, cornflakes, müsli, sylt, kokt ägg, prinskorv, stekt ägg, bacon, skinka, ost, rökt korv, marmelad, grönsaker, olika sorters frukostbullar, grovt bröd, rostat bröd och knäckebröd.",
    "details": "Inga detaljer tillgängliga",
    "category": 4,
    "allergens": [
      {
        "id": 4,
        "name": "ägg"
      }
    ]
  },
  {
    "id": 16,
    "name": "Frukostbuffé",
    "description": "Kaffe, the, choklad, filmjölk, mjölk, cornflakes, müsli, grovt bröd, frallor, ost, skinka, grönsaker, marmelad, ägg och juice.",
    "details": "Inga detaljer tillgängliga",
    "category": 4,
    "allergens": [
      {
        "id": 4,
        "name": "ägg"
      }
    ]
  },
  {
    "id": 17,
    "name": "Chilikassler",
    "description": "Med mild chiligräddsås, ananas och ris.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 18,
    "name": "Baconlindad köttfärslimpa",
    "description": "Med brunsås, kokt potatis, kokta grönsaker och lingonsylt.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 19,
    "name": "Plommonspäckad fläskkarré",
    "description": "Med gräddsås, kokt potatis, herrgårdsgrönsaker och äpplemos.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 20,
    "name": "Fläskschnitzel",
    "description": "Med bearnaisesås och klyftpotatis.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 21,
    "name": "Grillad kyckling",
    "description": "Med currysås, ris och coleslawsallad.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 22,
    "name": "Stekt spättafilé",
    "description": "Med remouladsås, kokt potatis och citron.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 23,
    "name": "Farmer Bill\u0027s Sallad",
    "description": "Tonfisk eller kyckling med caesarsallad, bacon, kokta ägg och brödkrutonger.",
    "details": "",
    "category": 5,
    "allergens": [
      {
        "id": 4,
        "name": "ägg"
      }
    ]
  },
  {
    "id": 24,
    "name": "Marinerad kycklingfilé",
    "description": "Med vildris, grönsaker och tomatsalsa.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 25,
    "name": "Lax",
    "description": "Med gröna kryddor, potatis, gröna ärtor och hollandaisesås.",
    "details": "",
    "category": 5,
    "allergens": []
  },
  {
    "id": 26,
    "name": "Tacobuffé",
    "description": "Härlig buffé med tacokryddad köttfärs, tacoskal, majs, riven ost, isbergssallad, gurka, tomat, rå lök, paprika, jalapeno, salsasås, guacamole och vitlöksdressing.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 27,
    "name": "Chili con carne",
    "description": "med bröd och smör.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 28,
    "name": "Grillad majskolv",
    "description": "med smör och salt.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 29,
    "name": "Grillad small korv",
    "description": "med bröd, ketchup och senap.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 30,
    "name": "Älgkebab",
    "description": "i pitabröd med lingonsylt, orientdressing och sallad.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 31,
    "name": "Stor Älgkebab",
    "description": "i pitabröd med lingonsylt, orientdressing och sallad.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 32,
    "name": "Hamburgertallrik 90 g",
    "description": "Hamburgare, bröd, sallad, dressing och pommes frites.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 33,
    "name": "Hamburgertallrik 150 g",
    "description": "Hamburgare, bröd, sallad, dressing och pommes frites.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 34,
    "name": "Kycklingtallrik",
    "description": "Kycklingklubba, pommes frites, sallad.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 35,
    "name": "Kebabtallrik",
    "description": "Kebabkött med pommes frites, sallad, tomat, gurka, rå lök, vitlöks/chilisås.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 36,
    "name": "Köttbullar med potatismos",
    "description": "Serveras med lingonsylt, tomatketchup, inlagd gurka och blandsallad",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 37,
    "name": "Pizza Wild West",
    "description": "Pizza med skinka, ost, tomat och ananas. Pizzasallad.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 38,
    "name": "Spagetti Bolognaise",
    "description": "Spagetti, köttfärssås, parmesan, ketchup och blandsallad.",
    "details": "",
    "category": 6,
    "allergens": []
  },
  {
    "id": 39,
    "name": "Baguette",
    "description": "med köttbullar, rödbetor och inlagd gurka.",
    "details": "",
    "category": 7,
    "allergens": []
  },
  {
    "id": 40,
    "name": "Baguette",
    "description": "med skinka, ost och fruktsallad.",
    "details": "",
    "category": 7,
    "allergens": []
  },
  {
    "id": 41,
    "name": "Baguette",
    "description": "med rostbiff, potatissallad, remouladsås och syltlök.",
    "details": "",
    "category": 7,
    "allergens": []
  },
  {
    "id": 42,
    "name": "Räksmörgås med ägg",
    "description": "",
    "details": "",
    "category": 7,
    "allergens": [
      {
        "id": 4,
        "name": "ägg"
      }
    ]
  },
  {
    "id": 43,
    "name": "Ost/skinkfralla",
    "description": "med grönsaker.",
    "details": "",
    "category": 7,
    "allergens": []
  },
  {
    "id": 44,
    "name": "Våffla",
    "description": "med vispgrädde och sylt",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 45,
    "name": "Kanelsnäcka",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 46,
    "name": "Brownie",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 47,
    "name": "Chocolate Chip Cookie",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 48,
    "name": "Morotskaka",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 49,
    "name": "Cheese Cake",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 50,
    "name": "Texas Bullar",
    "description": "",
    "details": "",
    "category": 8,
    "allergens": []
  },
  {
    "id": 51,
    "name": "Läsk",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 52,
    "name": "Mineralvatten",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 53,
    "name": "Kaffe med påtår",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 54,
    "name": "Te",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 55,
    "name": "Varm choklad med vispgrädde",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 56,
    "name": "Juice",
    "description": "",
    "details": "",
    "category": 9,
    "allergens": []
  },
  {
    "id": 57,
    "name": "Ananasbål",
    "description": "med lime och gurka.",
    "details": "",
    "category": 10,
    "allergens": []
  },
  {
    "id": 58,
    "name": "Lingonbål",
    "description": "med blåbär",
    "details": "",
    "category": 10,
    "allergens": []
  },
  {
    "id": 59,
    "name": "Jordnötter och snacks",
    "description": "med dipsåser.",
    "details": "",
    "category": 10,
    "allergens": [
      {
        "id": 3,
        "name": "nötter"
      }
    ]
  }
];
    
}]);
