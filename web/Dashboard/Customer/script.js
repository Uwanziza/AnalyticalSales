var today = new Date();
var minDate = today.setDate(today.getDate() + 1);

$('#datePicker').datetimepicker({
  useCurrent: false,
  format: "DD/MM/YYYY",
  minDate: minDate
});


$('#datePicker1').datetimepicker({
  useCurrent: false,
  format: "DD/MM/YYYY",
  minDate: minDate
});

var firstOpen = true;
var time;

$('#timePicker').datetimepicker({
  useCurrent: false,
  format: "hh:mm A"
}).on('dp.show', function() {
  if(firstOpen) {
    time = moment().startOf('day');
    firstOpen = false;
  } else {
    time = "01:00 PM"
  }
  
  $(this).data('DateTimePicker').date(time);
});

$('#timePicker1').datetimepicker({
  useCurrent: false,
  format: "hh:mm A"
}).on('dp.show', function() {
  if(firstOpen) {
    time = moment().startOf('day');
    firstOpen = false;
  } else {
    time = "01:00 PM"
  }
  
  $(this).data('DateTimePicker').date(time);
});