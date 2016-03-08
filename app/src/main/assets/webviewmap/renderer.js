function goToVitrini( vitrini ) 
{
  VitriniControllerInterface.goToVitrini(vitrini);
}

function centerMapInPoint(str_x, str_y)
{
  VitriniControllerInterface.centerMapInPoint(str_x, str_y);
}

var QUERY_FOR_MAP = "#map";
var PREFIX_LOCATION = "#location_";
var QUERY_FOR_ALL_LOCATIONS = ".location";
var CLASS_FOR_LOCATION = "location";
var CLASS_FOR_FOCUS = "focus";


var segmentsToConsider = [ 'comidas_e_bebidas', 'blog', 'festas'
	, 'entretenimento', 'lembrancas', 'moda', 'moveis'
	, 'produtos_servicos' ];

var segmentsLabels = ['Alimento e Bebida', 'Blogueiras', 'Decoração de Festas'
	,'Entretenimento', 'Lembranças', 'Moda'
	,'Móveis', 'Serviços e Produtos'];

var segmentColors = ['#FF0000', '#0061A1', '#F16633', '#40CC54', '#C8A700', '#EB4233', '#754C24', '#662D91']	


	/*moda -> EB4233
	festas -> F16633
	alimento -> FF0000
	entre --> 40CC54

	moveis 754C24

	lembrancas -> C8A700
	servicos -> 662D91
	blog - > 0061A1*/
	
function focusVitriniLocation ($location, vitriniName)
{
  $('.vitriniContainer').hide();

  var centerX = parseFloat($location.css('left').replace('px', ''));
  var centerY = parseFloat($location.css('top').replace('px', ''));
  if ( $location.text().length > 3 )
  {
    centerX = centerX + 20;
  }

  // if vitriniName is equal NULL, all vitrinis in the given location will be displayed. 
  // Otherwise, vitriniName is not NULL, just the vitrini with the given name will be displayed.
  var vitrini_count = $location.attr("vitrini_size");
  for(var index = 0; index < vitrini_count; ++index)
  {
    var name = $location.attr("originalName_" + index);
    if(vitriniName && vitriniName != name)
    {
      continue;
    }

    var vitrini_id = $location.attr("vitrini_id_" + index);
    var segmentLabel = $location.attr("segment_" + index);

    var segmentRandIndex = 0;
    for (var i = 0; i < segmentsLabels.length; i++)
    {
      if (segmentLabel == segmentsLabels[i])
      {
        segmentRandIndex = i;
        break;
      }
    }
    
    var segmentNameToConsider = segmentsToConsider[segmentRandIndex];
	  var segmentColor = segmentColors[segmentRandIndex];

    var $vitriniCtner = $("#vitriniContainer_" + index);
    if(vitrini_count == 1 || vitriniName)
    {
      $vitriniCtner.hide();
      $vitriniCtner.find('.vitriniBase').show();
	  
	  var $vitriniBox = $('.vitriniBox');
		$vitriniBox.css('background', 'url(tip_bg_ballon.png)');

	  	$vitriniCtner.css('top', centerY);
	   	$vitriniCtner.css('left', centerX);
    }
    else
    {
      $vitriniCtner.find('.vitriniBase').hide();
    
      // calculate vitrini coordinate in an elliptical way.
      var distributionPattern = [5, 4, 3, 0, 2, 1];
      var multTipHeight = 437;
      var radiusX = 405;
      var radiusY = 370;
      var maxVitrinis = 6
      var angleStep = (2 * Math.PI / maxVitrinis);
      
      var vLocalID = index % maxVitrinis; // prevent vitriniSize greater than 6 
      var x = centerX + (radiusX * Math.cos(distributionPattern[vLocalID] * angleStep));
      var y = centerY + (radiusY * Math.sin(distributionPattern[vLocalID] * angleStep)) + (multTipHeight / 2.0);
	  	
      $vitriniCtner.hide();
	  	$vitriniCtner.css('top', y);
	   	$vitriniCtner.css('left', x);
		
		var $vitriniBox = $('.vitriniBox');
		$vitriniBox.css('background', 'url(multi_tip_bg.png)');
    }

	  var $vitriniName = $vitriniCtner.find('.vitriniName');
	  $vitriniName.text(name);

	  //setting the image Name
	  var $vitriniSegmentIcon = $vitriniCtner.find('.vitriniSegmentIcon');
	  $vitriniSegmentIcon.attr('src', 'segments/' + segmentNameToConsider + '.png' );
	
    var $vitriniSegmentLabel = $vitriniCtner.find('.vitriniSegmentLbl');
		$vitriniSegmentLabel.text( segmentLabel );
		$vitriniSegmentLabel.css('color', segmentColor);
	  
    $vitriniCtner.show();
		
    var vitriniNameToConsider = $location.attr('originalName_' + index);
		var $vitriniBox = $vitriniCtner.find('.vitriniBox');
		$vitriniBox.attr('vitrini_name', vitriniNameToConsider );
		$vitriniBox.unbind ('click');
		$vitriniBox.bind ('click', 
			function ( event )
			{ 
				var vname = $(this).attr('vitrini_name');
				goToVitrini( vname );
				event.stopPropagation();
			}
		);
	}
  
	centerMapInPoint(centerX, centerY);
}
	
function showVitrini( vitriniId, vitriniName, locationId, segment ) 
{
	
	var $map = $( QUERY_FOR_MAP );   
	
	var locationQuery = PREFIX_LOCATION + locationId;
	
	var $locationIcon = $map.find( locationQuery );
	
	if ( $locationIcon.length == 0 )
	{
		return;
	}
	
	var vitrinis_length =  $locationIcon.attr('vitrini_size');
	
	if ( vitrinis_length == undefined )
	{
		vitrinis_length = 0;
	}
	
	
	$locationIcon.attr('vitrini_id_' + vitrinis_length, vitriniId );
	
	//adicionando o segmento
	$locationIcon.attr('segment_' + vitrinis_length, segment );
	
	$locationIcon.attr('originalName_' + vitrinis_length, vitriniName );
	
	//adicionando o nome
	var vitriniNameToStore = vitriniName.trim();
	vitriniNameToStore = vitriniNameToStore.toLowerCase();
	
	$locationIcon.attr('name_' + vitrinis_length, vitriniNameToStore );
	

	$locationIcon.unbind('click');
	$locationIcon.click( 
		function( eventLocationIcon )
		{
			var $location = $(this);
			
			focusVitriniLocation( $location ); 
			eventLocationIcon.stopPropagation();
		}
	);
	
	vitrinis_length++;
	$locationIcon.attr('vitrini_size', vitrinis_length);
	
	
}

function applySegmentFilter( segment )
{
	var $map = $( QUERY_FOR_MAP );   
	
	var locationQuery = QUERY_FOR_ALL_LOCATIONS;
	
	var $locationIcon = $map.find( locationQuery + '[segment="' +  segment +  '"]');
	
	var segmentToConsider = segment.trim();
	segmentToConsider = segmentToConsider.toLowerCase(); 
	$locationIcon.addClass( segmentToConsider );
	
	$locationIcon.addClass( 'strong' );
}

function resetSegmentFilter ()
{
	var $map = $( QUERY_FOR_MAP );   
	
	var locationQuery = QUERY_FOR_ALL_LOCATIONS;
	
	var $locations = $(locationQuery);
	
	//remove todas as classes
	$locations.removeClass();
	
	$locations.addClass( CLASS_FOR_LOCATION );
}

function focusVitrini( vitriniName )
{
	var $map = $( QUERY_FOR_MAP );   
	var locationQuery = QUERY_FOR_ALL_LOCATIONS;

	var vitriniNameToStore = vitriniName.trim();
	vitriniNameToStore = vitriniNameToStore.toLowerCase();

	var $vitriniLocation;
	
	for ( var index = 0; index < 5; index++ )
	{
		$vitriniLocation = $( locationQuery + '[name_' + index + '="' + vitriniNameToStore + '"]');
	
		if ( $vitriniLocation.length > 0  )
		{
			break;
		}
	}
	focusVitriniLocation( $vitriniLocation, vitriniName );
} 
 

$(document).ready( function()
{
	
	var $map = $( QUERY_FOR_MAP );
	$map.unbind('click');
	$map.bind('click', 
		function () 
		{
			var $vitriniCtner  = $('.vitriniContainer');
			$vitriniCtner.hide();
			
			var $locations = $(QUERY_FOR_ALL_LOCATIONS);
			//$locations.css('font-weight', 'initial');
			
		}
	);
	
	/*showVitrini('barco', 'Cervejaria basdas', 'espaco_mais', 'Complementos para casa');
	showVitrini('bodebrown', 'Cervejaria Bode Brown', 'espaco_mais', 'Infantil');
	showVitrini('bodebrown', 'Alessandro', 'espaco_mais', 'Infantil');
	showVitrini('bodebrown', 'Marcelo', 'espaco_mais', 'Infantil');
	showVitrini('bodebrown', 'Renato', 'espaco_mais', 'Infantil');
	showVitrini('bodebrown', 'Thiago Nunes', 'espaco_mais', 'Infantil');
	showVitrini('bodebrown', 'Thiago Nunes', '80', 'Infantil');*/
	
	
	//focusVitrini('Cervejaria Barco');
	//applySegmentFilter( 'Equipamentos' );
	//applySegmentFilter( 'Cervejaria' );
	//resetSegmentFilter();*/
});


$(window).load( function ()
{
	var $loading = $('#loadingCtner');
	$loading.remove();
});
