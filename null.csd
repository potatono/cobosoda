<CsoundSynthesizer>
<CsOptions>
csound -d -A -odevaudio null.csd
</CsOptions>
<CsInstruments>
sr = 44100
kr = 4410
ksmps = 10
nchnls = 2

0dbfs = 1.0

gitable1 ftgen 1, 0, 256, -7, 0.0, 64.0, 1.0, 128.0, -1.0, 64.0, 0.0
gitable2 ftgen 2, 0, 8192, 10, 1.0
gitable3 ftgen 3, 0, 16, -2, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0
gitable4 ftgen 4, 0, 32, -2, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0
gitable5 ftgen 5, 0, 512, 21, 2.0, 1.0

chn_a "chna0", 3
chn_a "chna1", 3
chn_a "chna2", 3
chn_a "chna3", 3
chn_a "chna4", 3
chn_a "chna5", 3
chn_a "chna6", 3
chn_a "chna7", 3
chn_a "chna8", 3
chn_a "chna9", 3

instr 1
aclear = 0.0
chnset aclear, "chna0"
aclear = 0.0
chnset aclear, "chna1"
aclear = 0.0
chnset aclear, "chna2"
aclear = 0.0
chnset aclear, "chna3"
aclear = 0.0
chnset aclear, "chna4"
aclear = 0.0
chnset aclear, "chna5"
endin

instr 2
kenv expseg 2, 0.14938, 15, 0, 8, 0.17, 22, 0, 13, 0.17, 28
aenv2 linseg 1, p3 - 0.02, 1, 0.2, 0
kphasor phasor 1 / 0.01625
ktrig trigger kphasor, 0.5, 0
ksamp samphold kenv, ktrig
a1 oscil 0.5, 1 / (0.01625 / int(ksamp)), gitable1, -1
chnmix a1 * aenv2 * p4, "chna0"
endin

instr 3
a1 oscil p4, p5, gitable2
aenv linseg 0, p3 * 0.05, 1, p3 * 0.95, 0
a1 = a1 * aenv
chnmix a1, "chna1"
endin

instr 4
endin

instr 5
iamp = p4
ifreq = p5
iwave = p6
ipan = p7
ifres = p8
iares = p9
iaa = p10
iadur1 = p11
iab = p12
iadur2 = p13
iac = p14
iarel = p15
iaiz = p16
ifa = p17
ifdur1 = p18
ifb = p19
ifdur2 = p20
ifc = p21
ifrel = p22
ifiz = p23
kenv expsegr ifa, ifdur1, ifb, ifdur2, ifc, ifrel, ifiz
kenv = kenv * ifres
kenv = int(kenv)
kenv = kenv / ifres
kenv = kenv - 1
kfreq = ifreq * (kenv + 1);
kenv2 linsegr iaa, iadur1, iab, iadur2, iac, iarel, iaiz
kenv2 = kenv2 * iares
kenv2 = int(kenv2)
kenv2 = kenv2 / iares
kamp = iamp * kenv2
aosc oscil kamp, kfreq, gitable3, -1
chnmix aosc * sqrt(1 - ipan), "chna2"
chnmix aosc * sqrt(ipan), "chna3"
endin

instr 6
amixer = 0.0
a1 chnget "chna1"
amixer = amixer + a1
a1 chnget "chna2"
amixer = amixer + a1
a1 chnget "chna0"
amixer = amixer + a1
chnset amixer, "chna4"
endin

instr 7
amixer = 0.0
a1 chnget "chna1"
amixer = amixer + a1
a1 chnget "chna3"
amixer = amixer + a1
a1 chnget "chna0"
amixer = amixer + a1
chnset amixer, "chna5"
endin

instr 8
a1 chnget "chna4"
a2 nreverb a1, 0.1, 0.5
chnset a1 * 0.875 + a2 * 0.125, "chna7"
endin

instr 9
a1 chnget "chna4"
a2 nreverb a1, 0.1, 0.4
chnset a1 * 0.874 + a2 * 0.126, "chna9"
endin

instr 10
a1 chnget "chna7"
a2 chnget "chna9"
outs a1, a2
endin

</CsInstruments>
<CsScore>
i 6 0.0 -1.0 
i 7 0.0 -1.0 
i 8 0.0 -1.0 
i 9 0.0 -1.0 
i 10 0.0 -1.0 
i 1 0 36000.0

</CsScore>
</CsoundSynthesizer>
