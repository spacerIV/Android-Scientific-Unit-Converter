use Modern::Perl;
use Perl6::Slurp;
use Data::Dumper;

my @lines = slurp 'groups.txt';

my $x = 1;
foreach ( @lines )
{
    chomp;
    my ( $name, $icon ) = split /\|/;
    
    my $filename = sprintf("%.2d%s.json",$x,$name);
    $filename =~ s/ /_/g;
    
    open ( my $fh, '>', $filename );
    
    print $fh <<EOF;
{
    "group": [
        {
            "icon": "$icon",
            "group": "$name"
        }
    ],    
    "units": [
        {
            "name": "METERS_PER_SQUARE_SECOND",
            "symbol": "m/s[squared]",
            "nicename": "meter/sec[squared]",
            "conversionunit": "",
            "times": "",
            "divide": ""
        }
    ]
}
EOF

    close $fh;
    $x++;
}
