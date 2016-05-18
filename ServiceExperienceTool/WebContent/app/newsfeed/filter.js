angular.module('newsfeed')

.filter('filterByTags', function() {
  return function(items, tags) {
    if (tags == null || tags.length === 0) {
      return items;
    }
    var filtered = [];
    (items || []).forEach(function(item) {
      var matches = tags.some(function(tagToFind) {
        var isMatched = false;
        (item.tagData || []).forEach(function(ownTag) {
          if(ownTag.text.match(new RegExp(tagToFind.text, 'i'))) {
        	  isMatched = true;
          }
        });
        return isMatched;
      });
      if (matches) {
        filtered.push(item);
      }
    });
    return filtered;
  };
});