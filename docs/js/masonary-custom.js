/* 
 * assan multipurpose template
 */

/**
 * Base js functions
 */

$(document).ready(function(){
    var $container = $('.mas-boxes');

    var gutter = 30;
    var min_width = 300;
    $container.imagesLoaded( function(){
        $container.masonry({
            itemSelector : '.mas-boxes-inner',
            gutterWidth: gutter,
            isAnimated: true,
              columnWidth: function( containerWidth ) {
                var box_width = (((containerWidth - 2*gutter)/3) | 0) ;

                if (box_width < min_width) {
                    box_width = (((containerWidth - gutter)/2) | 0);
                }

                if (box_width < min_width) {
                    box_width = containerWidth;
                }

                $('.mas-boxes-inner').width(box_width);

                return box_width;
              }
        });
    });
});
